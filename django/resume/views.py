from django.shortcuts import render
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.core.files.storage import default_storage
import re
import pdfplumber

# Create your views here.
@csrf_exempt
def upload_pdf(request):
    if request.method == 'POST' and request.FILES.get('file'):
        pdf_file = request.FILES['file']
        file_name = default_storage.save(pdf_file.name, pdf_file)

        resume_data = {
            'title': file_name,
            'name': '',
            'birthymd': '',
            'email': '',
            '연령': '',
            'hphonenum': '',
            'mphonenum': '',
            'addr': '',
            'education_data': [],
            'career_data': [],
            'intro': []  # 자기소개서 내용을 리스트로 초기화
        }

        excluded_patterns = [
            '한글', '한자', '종', '보통', '자동차', '운전', '면허증', '\(주\)', '\(', '\)', '4년제', '2년제', '3년제'
        ]

        def extract_information(text):
            # 성명, 출생년도, 주소, 전화, 이메일 추출
            name_match = re.search(r'성명\s+([\w\s]{1,5})\s+', text)
            if name_match:
                resume_data['name'] = name_match.group(1).strip()

            # 생년월일 추출
            birth_date_match = re.search(r'생년월일\s+(\d{4}년\s*\d{1,2}월\s*\d{1,2}일)', text)
            if birth_date_match:
                # 추출한 날짜 문자열
                date_str = birth_date_match.group(1).strip()
                # 숫자만 추출
                resume_data['birthymd'] = re.sub(r'\D', '', date_str)

            # 이메일 추출
            email_match = re.search(r'[Ee]-?mail\s*([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,})', text)
            if email_match:
                resume_data['email'] = email_match.group(1).strip()

            # 연령 추출
            age_match = re.search(r'연령\s+만\s+(\d+)세', text)
            if age_match:
                resume_data['연령'] = age_match.group(1).strip()

            # 휴대폰 번호 추출
            phone_match = re.search(r'휴\s*대\s*폰\s*(\d{1,3}-\d{1,4}-\d{1,4})', text)
            if phone_match:
                resume_data['hphonenum'] = phone_match.group(1).strip()

            # 자택전화 추출
            home_phone_match = re.search(r'자택전화\s*(\d{1,3}-\d{1,3}-\d{1,4})', text)
            if home_phone_match:
                resume_data['mphonenum'] = home_phone_match.group(1).strip()

            # 주소 추출
            address_match = re.search(r'주\s*소\s*((?:\b\w+\b\s*){0,5})', text)
            if address_match:
                resume_data['addr'] = address_match.group(1).strip()

            # 학력사항 추출
            education_section_match = re.search(r'학력사항\s*(.*?)\s*(?:교육사항|수행프로젝트|$)', text, re.DOTALL)
            if education_section_match:
                education_section = education_section_match.group(1).strip()
                # 학력정보 항목 추출
                education_matches = re.finditer(
                    r'(\d{2}\.\d{2})\s*~\s*(\d{2}\.\d{2})\s*(.*?)?\s*(\w+)\s*(.*?)?\s*(\w+)\s*(.*?)\s*(학사|졸업|재학|중퇴)?',
                    education_section)
                for match in education_matches:
                    education_data = {
                        'entymd': match.group(1),
                        'gradeymd': match.group(2),
                        'schoolname': match.group(4).strip(),
                        'major': match.group(6).strip(),
                        'gradeuateyn': match.group(8) if match.group(4) else ''
                    }
                    # 중복 검사
                    if education_data not in resume_data['education_data']:
                        resume_data['education_data'].append(education_data)

            # 경력 정보 추출
            career_section_match = re.search(r'직\s*장\s*경\s*력\s*사\s*항\s*(.*?)\s*(?:자격|면허|$)', text, re.DOTALL)
            if career_section_match:
                career_section = career_section_match.group(1).strip()
                # 경력 정보 항목 추출
                career_matches = re.finditer(
                    r'(\d{2}\.\d{2})\s*~\s*(\d{2}\.\d{2})\s*(.*?)\s*(\w+)\s*(.*?)?\s*(\w+)\s*(.*?)\s*(?:상세내용)?$',
                    career_section, re.DOTALL)
                for match in career_matches:
                    career_data = {
                        'entymd': match.group(1),
                        'quitymd' :match.group(2),
                        'compname': match.group(3).strip(),
                        'jobclass': match.group(4).strip(),
                        'maintask': match.group(5).strip(),
                        'jobposition': match.group(6).strip(),
                        '상세내용': match.group(7).strip() if match.group(7) else '',
                    }
                    # 중복 검사
                    if career_data not in resume_data['career_data']:
                        resume_data['career_data'].append(career_data)

            # 자기소개서 추출
            intro_match = re.search(r'자\s*기\s*소\s*개\s*서\s*[:：]?\s*(.+)', text, re.DOTALL)
            if intro_match:
                new_intro = intro_match.group(1).strip()
                # 중복되지 않도록 리스트에 추가
                if new_intro not in resume_data['intro']:
                    resume_data['intro'].append(new_intro)

            return resume_data


        with pdfplumber.open(pdf_file) as pdf:
            total_pages = len(pdf.pages)
            for i in range(total_pages):
                page = pdf.pages[i]
                text = page.extract_text()
                text = re.sub(r'\\n', ' ', text)  # 줄 바꿈을 공백으로 변환
                text = re.sub(r'[^가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9@.\[\]]/', ' ', text)  # 특정 문자만 남기고 나머지 제거

                for pattern in excluded_patterns:
                    text = re.sub(pattern, '', text)  # 불필요한 단어 제거

                text = re.sub(r'\s+', ' ', text).strip()  # 2개 이상의 연속된 공백을 1개로 변환

                page_data = extract_information(text)
                for key in page_data:
                    if isinstance(resume_data[key], list):
                        for item in page_data[key]:
                            if item not in resume_data[key]:
                                resume_data[key].append(item)
                    elif page_data[key] and resume_data[key] == '':
                        resume_data[key] = page_data[key]

                    # 마지막 페이지에서만 데이터를 추출하도록 설정
                    if i == total_pages - 1:
                        page_data = extract_information(text)

            print(resume_data)

        return JsonResponse({'message': 'File uploaded successfully!', 'resume_data': resume_data, 'file_name': file_name})
    return JsonResponse({'error': 'No file uploaded!'}, status=400)