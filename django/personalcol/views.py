import json
from io import BytesIO
from PIL import Image
import random
from django.http import JsonResponse, HttpResponse, FileResponse
import os
import numpy as np
import cv2
import tensorflow as tf
import mediapipe as mp
from django.views.decorators.csrf import csrf_exempt
import base64
from personalcol import models
import dlib

rPath = 'personalcol/static'  # 실제 경로로 바꿔주세요

mp_face_detection = mp.solutions.face_detection
mp_face_mesh = mp.solutions.face_mesh

model_path = rPath + '/models/vgg16_Face_model.h5'
print('model_path ===>>>',model_path)
mask_model = tf.keras.models.load_model(model_path)

landmark_predictor_path = rPath +'/models/shape_predictor_68_face_landmarks.dat'
if not os.path.isfile(landmark_predictor_path):
    raise FileNotFoundError(f"File not found: {landmark_predictor_path}")
landmark_predictor = dlib.shape_predictor(landmark_predictor_path)



@csrf_exempt
def cuttingImage(request):
    try:
        # 이미지 파일 받기
        file = request.FILES['imgfile']
        file_name = file.name
        file_path = os.path.join(rPath, 'img', file_name)

        # 파일 저장을 위한 디렉토리 생성
        os.makedirs(os.path.dirname(file_path), exist_ok=True)

        # 파일 쓰기
        with open(file_path, 'wb') as fp:
            for chunk in file.chunks():
                fp.write(chunk)

        # 이미지 읽기 (models에 정의된 imread_unicode 함수 사용)
        image = models.imread_unicode(file_path)

        # 이미지가 없는 경우 에러 응답
        if image is None:
            return JsonResponse({'status': 'error', 'message': '이미지를 읽지 못했습니다.'})

        # 얼굴 검출 및 자르기 (models에 정의된 detect_and_crop_face 함수 사용)
        cropped_face = models.detect_and_crop_face(image)

        # 검출된 얼굴이 없는 경우 에러 응답
        if cropped_face is None:
            return JsonResponse({'status': 'error', 'message': '얼굴을 검출하지 못했습니다.'})

        # 자른 얼굴 이미지 저장 경로 설정
        cropped_face_dir = os.path.join(rPath, 'cropped_img')
        os.makedirs(cropped_face_dir, exist_ok=True)
        cropped_face_path = os.path.join(cropped_face_dir, 'cropped_' + file_name)

        # OpenCV 이미지를 RGB로 변환 (PIL을 사용하여 이미지 저장하기 위해)
        cropped_face_rgb = cv2.cvtColor(cropped_face, cv2.COLOR_BGR2RGB)

        # PIL을 사용하여 이미지 저장
        cropped_image = Image.fromarray(cropped_face_rgb)
        cropped_image.save(cropped_face_path)

        # 파일로 응답
        response = FileResponse(open(cropped_face_path, 'rb'), content_type='image/jpeg')
        response['Content-Disposition'] = f'attachment; filename="cropped_{file_name}"'
        return response

    except Exception as e:
        # 예외 발생 시 에러 응답
        return JsonResponse({'status': 'error', 'message': str(e)})

@csrf_exempt
def base64toFile(request):
    try:
        # 파일 수신 시작을 로그로 기록

        # 요청에서 파일 추출
        file = request.FILES['afterimage']
        file_name = file.name  # 파일 이름 추출

        # 파일 저장 경로 설정
        file_path = os.path.join(rPath, 'afterimg', file_name)

        # 디렉토리 생성 (없는 경우에만 생성)
        os.makedirs(os.path.dirname(file_path), exist_ok=True)

        # 파일 쓰기
        with open(file_path, 'wb') as fp:
            for chunk in file.chunks():
                fp.write(chunk)

        # 파일 다운로드 응답 생성
        response = FileResponse(open(file_path, 'rb'), content_type='image/jpeg')
        response['Content-Disposition'] = f'attachment; filename="cropped_{file_name}"'
        return response

    except Exception as e:
        # 에러가 발생한 경우 로그 기록 후 에러 응답 반환
        print('에러났음')
        return JsonResponse({'status': 'error', 'message': str(e)})



@csrf_exempt
def detect_mask(request):
    # POST 요청인지 확인
    if request.method != 'POST':
        return JsonResponse({'error': '올바른 요청 방법이 아닙니다.'}, status=400)

    # 이미지 파일이 있는지 확인
    if 'imgfile' not in request.FILES:
        return JsonResponse({'error': '이미지 파일이 제공되지 않았습니다.'}, status=400)

    # 이미지 파일 읽기
    file = request.FILES['imgfile']
    try:
        image_data = np.frombuffer(file.read(), np.uint8)
        image = cv2.imdecode(image_data, cv2.IMREAD_COLOR)
        if image is None:
            return JsonResponse({'error': '이미지 디코딩에 실패했습니다.'}, status=400)
    except Exception as e:
        return JsonResponse({'error': str(e)}, status=400)

    # 얼굴 감지 모델 로드
    model_path = rPath + '/models/vgg16_Face_model.h5'
    model = tf.keras.models.load_model(model_path)
    mp_face_detection = mp.solutions.face_detection
    face_detection = mp_face_detection.FaceDetection(model_selection=1, min_detection_confidence=0.5)

    # Mediapipe를 이용해 이미지에서 얼굴 감지
    res = face_detection.process(cv2.cvtColor(image, cv2.COLOR_BGR2RGB))

    # 얼굴이 감지되지 않았을 경우 처리
    if not res.detections:
        with open('personalcol/static/img/faceNotDectected.png', "rb") as imageFile:
            faceNotDectectedImg = base64.b64encode(imageFile.read()).decode('utf-8')
        return JsonResponse({'message': '얼굴을 찾을 수 없습니다.', 'image': faceNotDectectedImg}, status=200)

    # 가장 큰 얼굴 영역 처리
    largest_detection = max(res.detections, key=lambda det: det.location_data.relative_bounding_box.width * det.location_data.relative_bounding_box.height)
    bboxC = largest_detection.location_data.relative_bounding_box
    ih, iw, _ = image.shape

    left = int(bboxC.xmin * iw)
    top = int(bboxC.ymin * ih)
    right = int((bboxC.xmin + bboxC.width) * iw)
    bottom = int((bboxC.ymin + bboxC.height) * ih)

    # 얼굴 영역 추출
    face_image = image[top:bottom, left:right]

    # 입력 이미지 크기 정의
    input_shape = (224, 224)

    # 얼굴 이미지 resize 및 전처리
    resized_image = cv2.resize(face_image, (input_shape[1], input_shape[0]))
    preprocessed_image = resized_image.astype('float32') / 255.0
    preprocessed_image = np.expand_dims(preprocessed_image, axis=0)

    # 모델로 예측
    predictions = model.predict(preprocessed_image)

    # 예측 결과 해석
    class_index = np.argmax(predictions, axis=1)[0]

    # 마스크 착용 여부에 따라 다른 응답 반환
    if class_index == 1:
        _, buffer = cv2.imencode('.jpg', image)
        jpg_as_text = base64.b64encode(buffer).decode('utf-8')
        with open('personalcol/static/img/takeOffMask.png', "rb") as imageFile:
            takeOffMaskImg = base64.b64encode(imageFile.read()).decode('utf-8')
        return JsonResponse({'message': '얼굴 영역에서 마스크 미착용이 감지되었습니다.', 'image': jpg_as_text}, status=200)
    else:
        with open('personalcol/static/img/takeOffMask.png', "rb") as imageFile:
            takeOffMaskImg = base64.b64encode(imageFile.read()).decode('utf-8')
        return JsonResponse({'message': '얼굴 영역에서 마스크 착용이 감지되었습니다.', 'image': takeOffMaskImg}, status=200)

    return JsonResponse({'error': '예상치 못한 오류가 발생했습니다.'}, status=500)





@csrf_exempt
def season_tone(request):
    # POST 요청인지 확인
    if request.method != 'POST':
        return JsonResponse({'error': '올바른 요청 방법이 아닙니다.'}, status=400)

    # 이미지 파일이 있는지 확인
    if 'imgfile' not in request.FILES:
        return JsonResponse({'error': '이미지 파일이 제공되지 않았습니다.'}, status=400)

    # 이미지 파일 읽기
    file = request.FILES['imgfile']
    try:
        image_data = np.frombuffer(file.read(), np.uint8)
        image = cv2.imdecode(image_data, cv2.IMREAD_COLOR)
        if image is None:
            return JsonResponse({'error': '이미지 디코딩에 실패했습니다.'}, status=400)
    except Exception as e:
        return JsonResponse({'error': str(e)}, status=400)
    print("--------------------------------------")
    face_detector = dlib.get_frontal_face_detector()
    analyzer = models.PersonalColorAnalyzer(face_detector, landmark_predictor)
    season, probabilities, skin_color, eye_color, image, face, face_mask = analyzer.analyze(image)
    print(f"계산된 시즌: {season}")
    probabilities.update({"season": season})
    print(season)
    # for season, prob in probabilities.items():
    #     print(f"{season}: {prob:.1f}%")
    # print(season)
    return JsonResponse(probabilities)






@csrf_exempt
def personal_predict(request):
    if request.method == 'POST':
        tf.compat.v1.disable_eager_execution()

        # TensorFlow session setup
        sess = tf.compat.v1.Session()
        saver = tf.compat.v1.train.import_meta_graph(rPath+"/models/model.meta")
        saver.restore(sess, tf.compat.v1.train.latest_checkpoint(rPath+"/models"))
        graph = tf.compat.v1.get_default_graph()

        X = graph.get_tensor_by_name('X:0')  # source
        Y = graph.get_tensor_by_name('Y:0')  # reference
        Xs = graph.get_tensor_by_name('generator/xs:0')  # output

        data = json.loads(request.body)
        before_image = data.get('before_image')
        makeup_image = data.get('makeup_image')

        # Base64 문자열을 이미지로 변환
        before_image_data = base64.b64decode(before_image.split(',')[1])
        makeup_image_data = base64.b64decode(makeup_image.split(',')[1])

        # NumPy 배열로 변환
        nparr1 = np.frombuffer(before_image_data, np.uint8)
        nparr2 = np.frombuffer(makeup_image_data, np.uint8)

        # OpenCV로 이미지 읽기 및 RGB 변환
        img1 = cv2.imdecode(nparr1, cv2.IMREAD_COLOR)
        img2 = cv2.imdecode(nparr2, cv2.IMREAD_COLOR)
        img1_rgb = cv2.cvtColor(img1, cv2.COLOR_BGR2RGB)
        img2_rgb = cv2.cvtColor(img2, cv2.COLOR_BGR2RGB)

        # align_faces 함수 호출 (얼굴 검출 및 정렬)
        img1_faces = models.align_faces(img1_rgb)
        img2_faces = models.align_faces(img2_rgb)

        if img1_faces and img2_faces:
            src_img = img1_faces[0]  # source image
            ref_img = img2_faces[0]  # reference image

            X_img = models.preprocess(src_img)
            X_img = np.expand_dims(X_img, axis=0)  # (256, 256, 3) -> (1, 256, 256, 3)

            Y_img = models.preprocess(ref_img)
            Y_img = np.expand_dims(Y_img, axis=0)  # batch dimension

            output = sess.run(Xs, feed_dict={
                X: X_img,
                Y: Y_img
            })

            output_img = models.postprocess(output[0])

            # 이미지 저장 시 RGB로 변환하여 저장
            output_img_bgr = cv2.cvtColor(output_img, cv2.COLOR_RGB2BGR)
            _, buffer = cv2.imencode('.jpg', output_img_bgr)
            image_base64 = base64.b64encode(buffer).decode('utf-8')

            response_data = {
                'image_base64': image_base64
            }
            return JsonResponse(response_data)
        else:
            print("Face alignment failed.")
            return JsonResponse({'error': 'Face alignment failed.'}, status=400)
