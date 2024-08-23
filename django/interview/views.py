import json
import random
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
import base64
import io
from PIL import Image
import numpy as np


from interview.models import perform_face_detection, stt_models, save_video, get_encoded_image, get_image_url, \
    convert_webm_to_mp4, extract_frames_from_video, analyze_pose, stt_models_from_video_path,make_transcript3,\
    stt_dict ,predict_emotion


rPath = 'interview/static'
model_path = rPath + '/models/vgg16_Face_model.h5'
video_path = 'media/videos'





@csrf_exempt
def face_detect(request):
    if request.method == 'POST':
        print('왔어요?')
        data = json.loads(request.body)
        image_data = data.get('image')

        if image_data:
            image_data = image_data.split(",")[1]
            image = Image.open(io.BytesIO(base64.b64decode(image_data)))
            image_np = np.array(image)

            # 얼굴 인식 처리
            face_detection_result = perform_face_detection(image_np)

            if face_detection_result :
                # 예시: 얼굴 인식 결과를 JSON으로 반환
                return JsonResponse({'result': 'Face detected!'})
            else:
                return JsonResponse({'result': 'Face detected fail'})

    return JsonResponse({'error': 'Invalid request'}, status=400)


@csrf_exempt
def speach_text(request):
    if request.method == 'POST':
        if 'audio' not in request.FILES:
            return JsonResponse({'error': 'No audio file provided'}, status=400)
        # speach to text 구현
        print('speach_text 왔나요~')
        audio_file = request.FILES['audio']
        # 파일의 크기와 이름을 출력하여 확인
        print(f"파일 이름: {audio_file.name}, 파일 크기: {audio_file.size} 바이트")

        stt_result=stt_models(audio_file);
        print(stt_result)
        if stt_result=='':
            stt_result='목소리가 인식되지 않았습니다.';
        return JsonResponse({'result': stt_result })

    return JsonResponse({'error': 'Invalid request method'}, status=405)

@csrf_exempt
def question_detail(request):
    if request.method == 'POST':
        # 업로드된 비디오 파일을 가져옵니다.
        video_file = request.FILES.get('video')
        mp4_url, unique_file_name = save_video(video_file)
        print('mp4_url=> ', mp4_url)
        webm_path = video_path + '/webm/' + unique_file_name + '.webm'
        mp4_path = video_path + '/mp4/' + unique_file_name + '.mp4'
        convert_webm_to_mp4(webm_path, mp4_path)
        frames = extract_frames_from_video(mp4_path)
        print('view1')
        #transcript_json, statistics_filler_json, statistics_silence_json=make_transcript3(mp4_path)
        print('view2')
        #dict_stt=stt_dict(transcript_json)
        #print('dict_stt', dict_stt)
        print('view3')
        stt_text=stt_models_from_video_path(mp4_path)
        print('view4')

        print('stt_text', stt_text)

        # 반복 호출 시 초기화가 필요한 변수들

        badcount = 0
        emotion_scores = []  # 초기화
        emotion_data = {}    # 초기화
        print('frame 갯수 지정 하기 전')

        if frames:
            num_frames = len(frames)
            seconds = min(num_frames, 89)  # 최대 89초까지 감정 할당

            for i in range(1, seconds + 1):
                frame = frames[i - 1]
                predicted_emotion, stress_score, category = predict_emotion(frame)

                # 감정 카테고리 매핑
                if category == '긍정':
                    emotion_value = 1
                elif category == '중립':
                    emotion_value = 0
                elif category == '부정':
                    emotion_value = -1
                else:
                    emotion_value = 0  # 처리되지 않은 감정 카테고리는 중립으로 처리
                    print(f"Unrecognized emotion category: {category}")

                # 감정 데이터 업데이트
                emotion_data[f"{i}sec"] = emotion_value
                emotion_scores.append(stress_score)

            print('frame for문 이후')

            # 감정 데이터 카운트
            ecntbad = list(emotion_data.values()).count(-1)
            ecntsoso = list(emotion_data.values()).count(0)
            ecntgood = list(emotion_data.values()).count(1)

            # 평균 스트레스 점수 계산
            avg_stress_score = sum(emotion_scores) / len(emotion_scores) if emotion_scores else 0
            avg_stress_score = int(avg_stress_score)

            # 자세 분석 및 점수 계산
            for frame in frames:
                frame_badcount = analyze_pose(frame)
                badcount += frame_badcount

            print("Total bad count:", badcount)
            print('len(frames)=>', len(frames))
            pscore = int((1 - (badcount / (len(frames) * 2))) * 100)
            print('pscore => ', pscore)

            # JSON 데이터 구성
            json_q_detail = {
                "answer": stt_text,
                "emotion": {
                    'escore': avg_stress_score,
                    **emotion_data,  # 감정 데이터를 먼저 추가
                    "ecntbad": ecntbad,
                    "ecntsoso": ecntsoso,
                    "ecntgood": ecntgood
                },
                "position": {
                    "pbadcnt": badcount,
                    "pscore": pscore,
                },
                "voice": {
                    "vhertz": 5.42,
                    "vjitter": 37.18,
                    "vspeed": 55.7,
                    "vscore": 91,
                    "vhertzimg": "base64로 인코딩(String타입)",
                    "vjitterimg": "(주파수만으로 충분할것같으면 생략)",
                },
                "video_url": mp4_url,  # 비디오 파일의 URL을 추가합니다
                "aifeedback": "ai가 피드백 해준 문장입니다."
            }

            # 결과 확인
            print(json_q_detail)

            return JsonResponse(json_q_detail)


@csrf_exempt
def interview_result(request):
    if request.method == 'POST':
        print('왔다')
        data = json.loads(request.body)
        print(data)
        # 데이터 처리 로직 추가
        response_data = {
            'sttresult1': '전체 질문에 대한 면접자 답변의 종합평가 문장입니다1',
            'sttresult2': '전체 질문에 대한 면접자 답변의 종합평가 문장입니다2'
        }
        return JsonResponse(response_data, status=200)

    return JsonResponse({'error': 'Invalid request method'}, status=405)



