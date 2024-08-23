import numpy as np
import base64
import subprocess
from django.core.files.storage import FileSystemStorage
from django.conf import settings
import uuid
import cv2
import mediapipe as mp
from django.http import JsonResponse
from pydub import AudioSegment
from pydub.silence import detect_silence , detect_nonsilent
from google.cloud import speech
import os
import io
import librosa
from config.settings import BASE_DIR
import speech_recognition as sr
from tensorflow.keras.models import load_model

# Mediapipe 솔루션 초기화
mp_face_detection = mp.solutions.face_detection
mp_drawing = mp.solutions.drawing_utils

# stt json
SERVICE_ACCOUNT_JSON = 'interview/static/json/myspeechtotext-431005-a07eb9df65a2.json'
os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = SERVICE_ACCOUNT_JSON

#ffpeg.exe 경로 설정 각 위치에 맞게 설정
AudioSegment.converter = "D:/ICTEDU/본인경로/bin/ffmpeg.exe"

filler_determine_model = load_model('interview/static/models/filler_classifier_n_mfcc50.h5')
filler_classifier_model = load_model('interview/static/models/mfcc_models50_append_slience.h5')
# MediaPipe Pose 모델 초기화
mp_pose = mp.solutions.pose
pose = mp_pose.Pose(static_image_mode=True, min_detection_confidence=0.5)
mp_drawing = mp.solutions.drawing_utils






def stt_models(audio_file):
    print('stt입장')
    # 오디오 파일을 메모리 내에서 처리
    try:
        audio_file.seek(0)
        audio = AudioSegment.from_file(audio_file)
        print('오디오 변환 성공')
    except Exception as e:
        print(f"오디오 파일 로드 실패: {e}")
        return JsonResponse({'error': f"오디오 파일 로드 실패: {e}"}, status=400)
    # 16비트 샘플로 변환
    audio = audio.set_sample_width(2)  # 2 bytes per sample = 16 bit
    audio = audio.set_channels(1)
    print('채널1 완료')
    # 변환된 오디오 파일을 메모리 내에서 처리
    mono_audio_io = io.BytesIO()
    audio.export(mono_audio_io, format="wav")
    mono_audio_io.seek(0)
    print('1')
    # Google Cloud Speech-to-Text 클라이언트 생성
    client = speech.SpeechClient()
    print('2')
    sample_rate = audio.frame_rate
    print('sample_rate',sample_rate)
    # 음성 파일을 바이너리 데이터로 읽기
    content = mono_audio_io.read()
    print('3')

    # 오디오 설정 (이 부분은 필요한 대로 변경 가능)
    audio = speech.RecognitionAudio(content=content)
    print('4')
    config = speech.RecognitionConfig(
        encoding=speech.RecognitionConfig.AudioEncoding.LINEAR16,
        sample_rate_hertz=sample_rate,
        language_code="ko-KR",  # 한국어 설정 (필요한 언어로 변경 가능)
        enable_automatic_punctuation=True  # 자동 구두점 추가
    )
    print('5')

    # Google Cloud Speech-to-Text API 호출
    try:
        response = client.recognize(config=config, audio=audio)
    except Exception as e:
        print(f"API 요청 실패: {e}")
    print('6')
    # 응답에서 텍스트 추출
    transcript = ""
    for result in response.results:
        transcript += result.alternatives[0].transcript
        print('x')
    print('transcript',transcript)

    return transcript

def extract_audio_to_memory(video_file_path):
    # ffmpeg를 사용하여 오디오를 추출하고, 이를 stdout으로 전달
    command = [
        'ffmpeg',
        '-i', video_file_path,
        '-vn',  # 비디오를 제외하고 오디오만 추출
        '-f', 'wav',
        '-acodec', 'pcm_s16le',
        '-ar', '16000',  # 오디오 샘플링 레이트를 16kHz로 설정 (Google STT 요구 사항)
        '-ac', '1',  # 모노 오디오로 변환
        'pipe:1'  # stdout으로 출력
    ]

    # subprocess를 통해 명령 실행
    process = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    audio_data, _ = process.communicate()

    # BytesIO로 오디오 데이터를 메모리에 로드
    return io.BytesIO(audio_data)

def stt_models_from_video_path(video_file_path):
    try:
        # 비디오 파일에서 오디오를 추출하여 메모리에 저장
        audio_io = extract_audio_to_memory(video_file_path)

        # stt_models 함수 사용
        transcript = stt_models(audio_io)
        return transcript

    except Exception as e:
        print(f"오디오 변환 실패: {e}")
        return JsonResponse({'error': f"오디오 변환 실패: {e}"}, status=400)

def perform_face_detection(image_np):
    # Mediapipe 얼굴 검출 솔루션 초기화
    with mp_face_detection.FaceDetection(min_detection_confidence=0.5) as face_detection:
        # Mediapipe는 RGB 이미지로 작동하므로, 이미지를 RGB로 변환
        rgb_image = cv2.cvtColor(image_np, cv2.COLOR_BGR2RGB)

        # Mediapipe를 사용하여 얼굴 검출
        results = face_detection.process(rgb_image)

        # 결과에서 얼굴 검출 여부 확인
        if results.detections:
            return True  # 얼굴이 검출된 경우
        else:
            return False  # 얼굴이 검출되지 않은 경우


def save_video(video_file):
    print(os.path.join(BASE_DIR, 'media'))
    fs = FileSystemStorage(location=os.path.join(settings.BASE_DIR, 'media/videos/webm'))

    # 기존 코드와 동일
    unique_file_name = f"{uuid.uuid4()}.{video_file.name.split('.')[-1]}"
    print(unique_file_name.split('.')[0])
    file_path = fs.save(unique_file_name, video_file)

    # 저장된 파일의 완전한 URL을 반환합니다
    mp4_url = f"http://192.168.0.221:9000/media/videos/mp4/{unique_file_name.split('.')[0]}.mp4"

    return mp4_url, unique_file_name.split('.')[0]

def image_to_base64(file_path):
    with open(file_path, "rb") as image_file:
        encoded_string = base64.b64encode(image_file.read()).decode('utf-8')
    return encoded_string

def get_encoded_image():
    fs = FileSystemStorage(location=os.path.join(settings.BASE_DIR, 'interview/static/img'))
    file_path = fs.path('voice1.png')
    encoded_image = image_to_base64(file_path)
    return encoded_image

def get_image_url():


    file_url = "http://192.168.0.221:9000/media/img/voice1.png"

    return file_url


def convert_webm_to_mp4(input_path, output_path):
    """
    .webm 파일을 .mp4로 변환합니다.

    :param input_path: 입력 파일 경로
    :param output_path: 출력 파일 경로
    """

    # 출력 디렉토리가 없으면 생성
    output_dir = os.path.dirname(output_path)
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    command = ['ffmpeg', '-i', input_path, output_path]


    try:
        subprocess.run(command, check=True, capture_output=True, text=True)
    except subprocess.CalledProcessError as e:
        print(f"Error during conversion: {e}")
        raise



def extract_frames_from_video(video_path):
    # 비디오 파일 열기
    video_capture = cv2.VideoCapture(video_path)
    if not video_capture.isOpened():
        raise ValueError("Could not open video file.")

    # FPS와 비디오 프레임 수 확인
    fps = video_capture.get(cv2.CAP_PROP_FPS)
    print(f"FPS: {fps}")

    # 프레임 번호 초기화
    frame_number = 0
    frames = []

    while True:
        ret, frame = video_capture.read()
        if not ret:
            break

        # 프레임을 1초마다 처리
        if frame_number % int(fps) == 0:
            # 프레임을 NumPy 배열로 저장
            frames.append(frame)

        frame_number += 1

    # 비디오 캡처 객체에서 리소스 해제
    video_capture.release()

    return frames


def get_line_angle(point1, point2):
    # 두 점 사이의 선의 각도 계산
    x1, y1 = point1
    x2, y2 = point2
    return np.degrees(np.arctan2(y2 - y1, x2 - x1))


def analyze_pose(frame):
    # MediaPipe Pose 모델에 이미지 프레임을 전달
    image_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    results = pose.process(image_rgb)


    badcount = 0

    if results.pose_landmarks:
        # 랜드마크 좌표를 NumPy 배열로 변환
        landmarks = results.pose_landmarks.landmark
        landmark_coords = np.array([(landmark.x, landmark.y) for landmark in landmarks])

        # 어깨와 눈 랜드마크 인덱스
        LEFT_SHOULDER = 11
        RIGHT_SHOULDER = 12
        LEFT_EYE = 1
        RIGHT_EYE = 4

        if len(landmark_coords) > max(LEFT_SHOULDER, RIGHT_SHOULDER, LEFT_EYE, RIGHT_EYE):
            # 어깨와 눈 좌표 가져오기
            left_shoulder = (landmark_coords[LEFT_SHOULDER][0], landmark_coords[LEFT_SHOULDER][1])
            right_shoulder = (landmark_coords[RIGHT_SHOULDER][0], landmark_coords[RIGHT_SHOULDER][1])
            left_eye = (landmark_coords[LEFT_EYE][0], landmark_coords[LEFT_EYE][1])
            right_eye = (landmark_coords[RIGHT_EYE][0], landmark_coords[RIGHT_EYE][1])

            # 어깨와 눈을 연결하는 선의 각도 계산
            shoulder_angle = abs(get_line_angle(left_shoulder, right_shoulder))
            eye_angle = abs(get_line_angle(left_eye, right_eye))


            # 기준 2: 어깨가 수평에서 너무 기울어져 있는 경우 (비대칭 어깨)
            if shoulder_angle < 173:  # 어깨 각도 임계값 설정
                print('수평에서 어깨가 너무 기울어져 있는 경우 => ', shoulder_angle)
                badcount += 1

            # 기준 3: 고개가 한쪽으로 기울어진 경우
            if abs(left_eye[1] - right_eye[1]) > 0.01:  # 눈의 높이 차이 임계값 설정
                print('고개가 한쪽으로 기울어진 경우 => ', abs(left_eye[1] - right_eye[1]))
                badcount += 1



    return badcount

def STT_with_json_C(audio_file, jsons):
  first_silence = 0
  num = 0
  unrecognizable_start = 0
  r = sr.Recognizer()
  transcript_json = []
  statistics_filler_json = []
  statistics_silence_json = []
  filler_1 = 0
  filler_2 = 0
  filler_3 = 0
  audio_total_length = audio_file.duration_seconds
  silence_interval = 0
  for json in jsons :
    if json['tag'] == '0000':
      # 통역 개시 지연시간
      if num == 0: #
        first_silence = first_silence + (json['end']-json['start'])/1000 # 시작 무음 시간 (초단위)
      else:
        silence_interval = silence_interval + (json['end']-json['start'])/1000
        if ((json['end']-json['start'])/1000) > 1.4:
          silence = "(" + str(round((json['end']-json['start'])/1000-0.4)) + "초).."
          transcript_json.append({'start':json['start'],'end':json['end'],'tag':'0000','result':silence})
        # 무음시간 결과 append

    elif json['tag'] == '1111':
      # 추임새
      # 통역 개시 지연시간
      if num == 0:
        if first_silence>0:
          if ((json['end']-json['start'])/1000) > 1.4:
            silence = "(" + str(round((json['end']-json['start'])/1000-0.4)) + "초).."
            transcript_json.append({'start':json['start'],'end':json['end'],'tag':'0000','result':silence})
          first_silence_interval = first_silence
          num = num + 1
      # 추임새(어, 음, 그) 구분
      filler_type = predict_filler_type(audio_file[json['start']:json['end']]) #추임새 부분 모델 사용
      if filler_type == 0 :
        transcript_json.append({'start':json['start'],'end':json['end'],'tag':'1001','result':'어(추임새)'})
        filler_1 = filler_1 + 1
      elif filler_type == 1:
        silence_interval = silence_interval + (json['end']-json['start'])/1000
        if ((json['end']-json['start'])/1000) > 1.2:
          silence = "(" + str(round((json['end']-json['start'])/1000-0.2)) + "초).."
          transcript_json.append({'start':json['start'],'end':json['end'],'tag':'0000','result':silence})
        filler_2 = filler_2 + 1
      else:
        transcript_json.append({'start':json['start'],'end':json['end'],'tag':'1100','result':'그(추임새)'})
        filler_3 = filler_3 + 1

    elif json['tag'] == '1000':

      #인식불가 처리
      if unrecognizable_start != 0:
        audio_file[unrecognizable_start:json['end']].export("temp.wav", format="wav")
      else:
        audio_file[json['start']:json['end']].export("temp.wav", format="wav")
      temp_audio_file = sr.AudioFile('temp.wav')
      with temp_audio_file as source:
        audio = r.record(source)
      try :
        stt = r.recognize_google(audio_data = audio, language = "ko-KR")

        if num == 0:
          # 첫 무음시간 append
          if first_silence>0:
            if ((json['end']-json['start'])/1000) > 1.4:
              silence = "(" + str(round((json['end']-json['start'])/1000-0.4)) + "초).."
              transcript_json.append({'start':json['start'],'end':json['end'],'tag':'0000','result':silence})
            first_silence_interval = first_silence
            num = num + 1


        if unrecognizable_start != 0:
          print('오류발생 재시작 :',stt)
          transcript_json.append({'start':unrecognizable_start,'end':json['end'],'tag':'1000','result':stt})
        else:
          print('stt append :',stt)
          transcript_json.append({'start':json['start'],'end':json['end'],'tag':'1000','result':stt})
        unrecognizable_start = 0
        print('완료')

      except:
        #오류 발생 발생한 시작지점 저장
        print('stt오류발생')
        print(json['start'])
        print(json['end'])
        if unrecognizable_start == 0:
          unrecognizable_start = json['start']

  statistics_filler_json.append({'어':filler_1, '음':filler_2, '그':filler_3})
  statistics_silence_json.append({'통역개시지연시간':100 * first_silence_interval/audio_total_length, '침묵시간':100 * silence_interval/audio_total_length, '발화시간':100 * (audio_total_length - first_silence - silence_interval)/audio_total_length})
  return transcript_json, statistics_filler_json, statistics_silence_json


def create_json_C(audio_file):  # 커스텀해보기 거의 최종 커스텀시도
    intervals_jsons = []  # 결과를 저장할 리스트, 각 구간의 시작과 끝 시간을 JSON 형식으로 저장

    min_silence_length = 500  # 무음 구간을 감지하기 위한 최소 무음 길이 (밀리초 단위)
    intervals = detect_nonsilent(audio_file,
                                 min_silence_len=min_silence_length,
                                 silence_thresh=-33
                                 )  # 비무음 구간을 감지, 비무음 구간들의 시작과 끝 시간을 반환
    len_set=180
    # 첫 구간이 무음으로 시작하지 않으면, 시작 부분의 무음 구간을 JSON에 추가
    if intervals[0][0] != 0:
        intervals_jsons.append({'start': 0, 'end': intervals[0][0], 'tag': '0000'})  # tag: 0000 무음

    non_silence_start = intervals[0][0]  # 첫 비무음 구간의 시작 시간 934
    before_silence_start = intervals[0][1]  # 첫 비무음 구간의 끝 시간 4962
    print(intervals)
    for interval in intervals:
        if interval[0]<len_set:
            len_set=0
        interval_audio = audio_file[interval[0] - len_set:interval[1] + len_set]  # 현재 비무음 구간의 오디오 추출 silence_thresh=-32수치의 낮은 음성을 대비하여 구간을 넓힘
        print('구간 음성의 평균 음량', interval_audio.dBFS)
        if len(interval_audio) > 800:  # 사실상 800ms보다 좀 더 큰 음성 일 때
            intervals_jsons.append(
                {'start': interval[0] - len_set, 'end': interval[1] + len_set, 'tag': '1000'})  # tag: 1000 means non-slience
            if before_silence_start == interval[1]:
                continue
            else:
                intervals_jsons.append({'start': before_silence_start + len_set, 'end': interval[0] - len_set,
                                        'tag': '0000'})  # tag: 0000 means slience
            filler_audio1 = audio_file[interval[0] - len_set:interval[0] + len_set]
            filler_audio2 = audio_file[interval[1] - len_set:interval[1] + len_set]
            if predict_filler(filler_audio1) == 0:  # 추임새인 경우
                intervals_jsons.append({'start': interval[0] - len_set, 'end': interval[0] + len_set, 'tag': '1111'})
            if predict_filler(filler_audio2) == 0:  # 추임새인 경우
                intervals_jsons.append({'start': interval[1] - len_set, 'end': interval[1] + len_set, 'tag': '1111'})
        else:
            if predict_filler(interval_audio) == 0:  # 추임새인 경우
                intervals_jsons.append({'start': interval[0] - len_set, 'end': interval[1] + len_set, 'tag': '1111'})
            else:  # 아닐경우
                intervals_jsons.append({'start': interval[0] - len_set, 'end': interval[1] + len_set, 'tag': '1000'})

        len_set=180
        before_silence_start = interval[1]

    return intervals_jsons

def stt_dict(transcript_json):
    sorted_transcripts = sorted(transcript_json, key=lambda x: x['end'])
    startlist = []
    endlist = []
    resultlist = []
    taglist = []
    for transcript in sorted_transcripts:
        # print(transcript)
        if transcript['start'] in startlist:
            idx = startlist.index(transcript['start'])
            if transcript['end'] > endlist[idx]:
                endlist[idx] = transcript['end']
                resultlist[idx] = transcript['result']
                taglist[idx] = transcript['tag']
            elif transcript['end'] <= endlist[idx]:
                if transcript['tag'] == '1000':
                    startlist.append(transcript['start'])
                    endlist.append(transcript['end'])
                    resultlist.append(transcript['result'])
                    taglist.append(transcript['tag'])
        else:
            if transcript['result'] == "(음)" and len(resultlist) > 1 and resultlist[-1] == "(음)":
                endlist[-1] = transcript['end']
            else:
                startlist.append(transcript['start'])
                endlist.append(transcript['end'])
                resultlist.append(transcript['result'])
                taglist.append(transcript['tag'])
    text = ' '.join(resultlist)

    transcripts_combined = []
    for i in range(len(startlist)):
        transcripts_combined.append({
            'start': startlist[i],
            'end': endlist[i],
            'tag': taglist[i],
            'result': resultlist[i]

        })
    final_dict = {  # 프론트로 보낼 딕셔너리
        'transcripts': transcripts_combined,
        'text': text
    }

    return final_dict


def make_transcript3(mp4_path):
    print('make_transcript3입장')
    audio_io=extract_audio_to_memory(mp4_path)
    print('extract_audio_to_memory완료')
    try:
        audio = AudioSegment.from_file(audio_io, format="wav") #
    except Exception as e:
        raise RuntimeError(f"An error occurred while processing the audio file: {str(e)}")
    print('normalized_audio')
    normalized_audio = match_target_amplitude(audio, -11.0)  # 수치 -10에 가깝게 변환
    print('normalized_audio: ' + str(normalized_audio.dBFS))

    intervals_jsons = create_json_C(normalized_audio)
    print('intervals_jsons:', intervals_jsons)

    transcript_json = STT_with_json_C(normalized_audio, intervals_jsons)

    return transcript_json

def match_target_amplitude(sound, target_dBFS):
  # sound는 오디오 변환된 음성 데이터
  print('음성 파일의 평균 음량',sound.dBFS)
  change_in_dBFS = target_dBFS - sound.dBFS # sound.dBFS => 음성 데이터의 평균 음량 음수의 값을 가짐
  return sound.apply_gain(change_in_dBFS) #음성 데이터의 음량을 조절 change_in_dBFS가 -3이면 음성 데이터의 음량을 -3 줄여줌

def predict_filler(audio_file):
  pad2d = lambda a, i: a[:, 0:i] if a.shape[1] > i else np.hstack((a, np.zeros((a.shape[0], i - a.shape[1]))))
  # 추임새 판별을 위한 임시 음성 파일 생성
  audio_file.export("temp.wav", format="wav")

  wav, sr = librosa.load("temp.wav", sr=16000)

  mfcc = librosa.feature.mfcc(y=wav, sr=sr, n_mfcc=50)
  padded_mfcc = pad2d(mfcc, 40)
  padded_mfcc = np.expand_dims(padded_mfcc, 0)

  result = filler_determine_model.predict(padded_mfcc)

  # 판별 완료된 음성 파일 삭제
  os.remove("temp.wav")

  if result[0][0] >= result[0][1]: # 추임새
    return 0
  else:
    return 1

def predict_filler_type(audio_file):
  pad2d = lambda a, i: a[:, 0:i] if a.shape[1] > i else np.hstack((a, np.zeros((a.shape[0], i - a.shape[1]))))
  frame_length = 0.025
  frame_stride = 0.0010
  # 추임새 종류 판별을 위한 임시 음성 파일 생성
  audio_file.export("temp.wav", format="wav")

  wav, sr = librosa.load("temp.wav", sr=16000)
  #input_nfft = int(round(sr*frame_length))
  #input_stride = int(round(sr*frame_stride))

  mfcc = librosa.feature.mfcc(y=wav, sr=sr, n_mfcc=50)
  padded_mfcc = pad2d(mfcc, 40)
  padded_mfcc = np.expand_dims(padded_mfcc, 0)

  result = filler_classifier_model.predict(padded_mfcc)

  # 판별 완료된 음성 파일 삭제
  os.remove("temp.wav")

  return np.argmax(result)



import numpy as np
import cv2
from tensorflow.keras.models import load_model

# 모델 경로 설정
MODEL_PATH_Emotion = r'interview/static/models/saved_model_CNN0820.h5'



# 모델 로드
try:
    model_emotion = load_model(MODEL_PATH_Emotion)
    print("모델이 성공적으로 로드되었습니다.")
except Exception as e:
    print(f"모델 로드 오류: {e}")

# 감정에 따른 스트레스 점수 설정 (0~1 범위)
emotion_weights = {
    'happy': 0.1,
    'surprise': 0.3,
    'neutral': 0.4,
    'sad': 0.6,
    'fear': 0.8,
    'disgust': 0.7,
    'angry': 1.0
}

# 감정 카테고리 설정
emotion_categories = {
    '긍정': ['happy', 'surprise'],
    '중립': ['neutral'],
    '부정': ['sad', 'fear', 'disgust', 'angry']
}


def preprocess_image(image_array, target_size=(128, 128)):
    """
    이미지를 전처리하여 모델 입력 형식에 맞게 변환합니다.
    """
    img = cv2.resize(image_array, target_size)
    img_array = img.astype('float32') / 255.0  # 정규화
    img_array = np.expand_dims(img_array, axis=0)  # 배치 차원 추가
    print("전처리된 이미지 배열:", img_array.shape)
    return img_array


def determine_emotion_category(emotion_probs):
    """
    감정 확률값을 사용하여 긍정, 중립, 부정 카테고리로 분류합니다.
    감정 카테고리의 점수를 예민하게 구분하고 중립을 우선적으로 고려합니다.
    """
    positive_score = emotion_probs[0] + emotion_probs[1]  # happy + surprise
    neutral_score = emotion_probs[2]  # neutral
    negative_score = sum(emotion_probs[3:])  # sad + fear + disgust + angry

    # 각 카테고리의 상대 점수를 비교합니다.
    total_score = positive_score + neutral_score + negative_score

    if total_score == 0:
        return '중립'  # 모든 점수가 0인 경우 중립으로 간주

    # 비율 기반 판단을 조정하여 중립의 확률을 높이고 긍정과 부정의 확률을 조정합니다.
    positive_ratio = positive_score / total_score
    negative_ratio = negative_score / total_score
    neutral_ratio = neutral_score / total_score

    # 중립을 우선적으로 고려하고, 긍정과 부정의 비율 기준을 조정합니다.
    if neutral_ratio > 0.4:
        return '중립'
    elif positive_ratio > 0.3:
        return '긍정'
    elif negative_ratio > 0.3:
        return '부정'
    else:
        # 비율이 비슷할 경우 중립으로 분류
        return '중립'


def predict_emotion(image_array):
    """
    입력된 이미지에서 감정을 예측하고, 해당 감정의 스트레스 점수를 계산한 후, 감정을 카테고리로 분류합니다.
    """
    img_array = preprocess_image(image_array)

    try:
        predictions = model_emotion.predict(img_array)
        print("예측 결과:", predictions)
    except Exception as e:
        print(f"예측 오류: {e}")
        return 'unknown', 0, 'unknown'

    emotion_labels = ['happy', 'surprise', 'neutral', 'sad', 'fear', 'disgust', 'angry']
    emotion_probs = predictions[0]

    # 각 감정에 대한 가중치를 적용하여 스트레스 점수 계산
    weighted_scores = [emotion_weights[emotion_labels[i]] * emotion_probs[i] for i in range(len(emotion_labels))]
    total_stress_score = np.sum(weighted_scores) * 100

    # 감정 카테고리 결정 (확률 기반)
    category = determine_emotion_category(emotion_probs)

    # 가장 높은 확률을 가진 감정 선택
    predicted_emotion = emotion_labels[np.argmax(emotion_probs)]

    return predicted_emotion, total_stress_score, category


