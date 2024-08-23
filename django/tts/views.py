import os
import json
import io
from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt
from google.cloud import texttospeech
from google.oauth2 import service_account

# JSON 파일 경로 설정 tts json
SERVICE_ACCOUNT_JSON = 'tts/static/json/myspeechtotext-431005-ca3313ced75e.json'

# 서비스 계정 키 정보를 사용하여 Credentials 객체 생성
def get_credentials():
    try:
        with open(SERVICE_ACCOUNT_JSON, 'r') as f:
            credentials_info = json.load(f)
        return service_account.Credentials.from_service_account_info(credentials_info)
    except Exception as e:
        print(f"Error loading credentials: {e}")
        return None

@csrf_exempt
def text_to_speech(request):
    credentials = get_credentials()
    print("1")
    if credentials is None:
        print("10")
        return JsonResponse({"error": "Credentials not loaded"}, status=500)

    if request.method == 'POST':
        try:
            data = json.loads(request.body)
            text = data.get('text', '')
            print("2",text)
            client = texttospeech.TextToSpeechClient(credentials=credentials)

            synthesis_input = texttospeech.SynthesisInput(text=text)

            voice = texttospeech.VoiceSelectionParams(
                language_code="ko-KR",
                ssml_gender=texttospeech.SsmlVoiceGender.FEMALE,
                name="ko-KR-Wavenet-B"
            )

            audio_config = texttospeech.AudioConfig(
                audio_encoding=texttospeech.AudioEncoding.MP3,
                speaking_rate=1.12,
                pitch=0.9
            )
            print("3")
            response = client.synthesize_speech(
                input=synthesis_input,
                voice=voice,
                audio_config=audio_config
            )

            audio_content = response.audio_content
            print("반환까지 완료됨")
            return HttpResponse(
                io.BytesIO(audio_content),
                content_type='audio/mpeg'
            )
        except Exception as e:
            print(f"Error synthesizing speech: {e}")
            return JsonResponse({"error": "Failed to synthesize speech"}, status=500)
    return JsonResponse({"error": "Invalid request"}, status=400)