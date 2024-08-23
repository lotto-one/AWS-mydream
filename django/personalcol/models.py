import cv2
import numpy as np
import tensorflow as tf
import mediapipe as mp
import dlib
from sklearn.metrics.pairwise import euclidean_distances

rPath = 'personalcol/static'  # 실제 경로로 바꿔주세요

mp_face_detection = mp.solutions.face_detection
mp_face_mesh = mp.solutions.face_mesh
face_detector = dlib.get_frontal_face_detector()


def predict(img1, img2):
    tf.compat.v1.disable_eager_execution()

    with tf.compat.v1.Session() as sess:
        # 모델 경로 설정
        model_path = rPath + "/models"
        print(model_path)
        meta_file_path = f"{model_path}/model.meta"

        # 메타 그래프 파일을 import_meta_graph로 불러오기
        saver = tf.compat.v1.train.import_meta_graph(meta_file_path)

        # 최신 체크포인트 파일 경로 확인
        checkpoint = tf.compat.v1.train.latest_checkpoint(model_path)
        if checkpoint is None:
            raise ValueError(f"No checkpoint found in directory: {model_path}")

        saver.restore(sess, checkpoint)
        graph = tf.compat.v1.get_default_graph()

        # 그래프에서 텐서 가져오기
        X = graph.get_tensor_by_name('X:0')
        Y = graph.get_tensor_by_name('Y:0')
        Xs = graph.get_tensor_by_name('generator/xs:0')

        # 이미지 형식 확인과 변환 (BGR을 RGB로)
        img1 = cv2.cvtColor(img1, cv2.COLOR_BGR2RGB)
        img2 = cv2.cvtColor(img2, cv2.COLOR_BGR2RGB)

        # 얼굴 정렬 함수 호출 (align_faces 함수는 따로 정의되어 있는 것으로 가정)
        img1_faces = align_faces(img1)
        img2_faces = align_faces(img2)

        # 얼굴 검출 실패 시 예외 처리
        if not img1_faces or not img2_faces:
            raise ValueError("Face detection failed for one or both images.")

        # 첫 번째 얼굴 이미지 선택
        src_img = img1_faces[0]
        ref_img = img2_faces[0]

        # 이미지 크기 조정
        src_img = cv2.resize(src_img, (256, 256))
        ref_img = cv2.resize(ref_img, (256, 256))

        # 전처리 함수 호출
        X_img = preprocess(src_img)
        X_img = np.expand_dims(X_img, axis=0)

        Y_img = preprocess(ref_img)
        Y_img = np.expand_dims(Y_img, axis=0)

        # 모델 예측 수행
        output = sess.run(Xs, feed_dict={
            X: X_img,
            Y: Y_img
        })

        # 후처리 함수 호출하여 출력 이미지 반환
        output_img = postprocess(output[0])

        return output_img


def align_faces(img):  # function to align faces using MediaPipe
    with mp_face_detection.FaceDetection(model_selection=1, min_detection_confidence=0.5) as face_detection:
        with mp_face_mesh.FaceMesh(static_image_mode=True, max_num_faces=1, refine_landmarks=True,
                                   min_detection_confidence=0.5) as face_mesh:
            results = face_detection.process(img)
            if results.detections:
                for detection in results.detections:
                    bboxC = detection.location_data.relative_bounding_box
                    ih, iw, _ = img.shape
                    x, y, w, h = int(bboxC.xmin * iw), int(bboxC.ymin * ih), int(bboxC.width * iw), int(
                        bboxC.height * ih)

                    # Adjust the crop coordinates to stay within image boundaries
                    x1 = max(x - 50, 0)
                    y1 = max(y - 50, 0)
                    x2 = min(x + w + 50, iw)
                    y2 = min(y + h + 50, ih)

                    cropped_face = img[y1:y2, x1:x2]
                    face_roi_resized = cv2.resize(cropped_face, (256, 256))  # Resize to 256x256
                    # face_roi_resized_rgb = cv2.cvtColor(face_roi_resized, cv2.COLOR_BGR2RGB)  # Convert to RGB
                    return [face_roi_resized]
    return []




def preprocess(img):
    return img.astype(np.float32) / 127.5 - 1


def postprocess(img):
    return ((img + 1.) * 127.5).astype(np.uint8)





def detect_and_crop_face(image):
    mp_face_detection = mp.solutions.face_detection
    with mp_face_detection.FaceDetection(model_selection=1, min_detection_confidence=0.5) as face_detection:
        results = face_detection.process(cv2.cvtColor(image, cv2.COLOR_BGR2RGB))
        if not results.detections:
            return None
        for detection in results.detections:
            bboxC = detection.location_data.relative_bounding_box
            ih, iw, _ = image.shape
            x, y, w, h = (bboxC.xmin * iw, bboxC.ymin * ih, bboxC.width * iw, bboxC.height * ih)
            x, y, w, h = int(x), int(y), int(w), int(h)
            # Adjust the crop coordinates to stay within image boundaries
            x1 = max(x - 50, 0)
            y1 = max(y - 50, 0)
            x2 = min(x + w + 50, iw)
            y2 = min(y + h + 50, ih)
            cropped_face = image[y1:y2, x1:x2]
            return cropped_face
    return None


def imread_unicode(filepath):
    stream = open(filepath.encode("utf-8"), "rb")
    bytes = bytearray(stream.read())
    np_array = np.asarray(bytes, dtype=np.uint8)
    return cv2.imdecode(np_array, cv2.IMREAD_UNCHANGED)


class PersonalColorAnalyzer:
    def __init__(self, face_detector, landmark_predictor):
        print("PersonalColorAnalyzer생성")
        self.face_detector = face_detector
        self.landmark_predictor = landmark_predictor
        self.seasons = {
            "Spring": {
                "skin": [(251, 211, 168), (255, 202, 149), (253, 197, 161), (252, 204, 130)],
                "eye": [(179, 134, 48), (157, 92, 18)]
            },
            "Summer": {
                "skin": [(253, 231, 174), (255, 219, 192), (254, 217, 170), (254, 210, 122)],
                "eye": [(111, 86, 40), (145, 112, 28)]
            },
            "Autumn": {
                "skin": [(255, 221, 150), (247, 206, 152), (249, 201, 128), (212, 169, 101)],
                "eye": [(157, 114, 12), (134, 96, 3)]
            },
            "Winter": {
                "skin": [(255, 220, 147), (242, 206, 148), (247, 207, 121), (216, 173, 102)],
                "eye": [(157, 111, 10), (136, 101, 10)]
            }
        }
        self.skin_weight = 0.7
        self.eye_weight = 0.3

    def extract_colors(self, image):
        # 이미지 로드
        # image = cv2.imread(image_path)
        # image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

        # 얼굴 감지
        faces = self.face_detector(image)
        if len(faces) == 0:
            raise ValueError("얼굴을 감지할 수 없습니다.")

        face = faces[0]
        landmarks = self.landmark_predictor(image, face)

        # 전체 얼굴 영역에서 피부색 추출
        face_mask = np.zeros(image.shape[:2], dtype=np.uint8)
        points = []
        for i in range(17):  # 얼굴 윤곽선
            points.append((landmarks.part(i).x, landmarks.part(i).y))
        for i in range(26, 16, -1):  # 얼굴 윤곽선 (반대 방향)
            points.append((landmarks.part(i).x, landmarks.part(i).y))

        points = np.array(points, dtype=np.int32)
        cv2.fillPoly(face_mask, [points], 255)

        # 눈, 눈썹, 입 영역 제외
        eyes_mouth_mask = np.zeros(image.shape[:2], dtype=np.uint8)
        for i in range(36, 48):  # 눈
            cv2.circle(eyes_mouth_mask, (landmarks.part(i).x, landmarks.part(i).y), 5, 255, -1)
        for i in range(48, 68):  # 입
            cv2.circle(eyes_mouth_mask, (landmarks.part(i).x, landmarks.part(i).y), 5, 255, -1)
        for i in range(17, 27):  # 눈썹
            cv2.circle(eyes_mouth_mask, (landmarks.part(i).x, landmarks.part(i).y), 5, 255, -1)

        face_mask = cv2.subtract(face_mask, eyes_mouth_mask)
        skin_color = cv2.mean(image, mask=face_mask)[:3]

        # 눈동자색 추출
        left_eye = np.mean(image[landmarks.part(37).y:landmarks.part(41).y,
                                 landmarks.part(36).x:landmarks.part(39).x], axis=(0, 1))
        right_eye = np.mean(image[landmarks.part(43).y:landmarks.part(47).y,
                                  landmarks.part(42).x:landmarks.part(45).x], axis=(0, 1))
        eye_color = np.mean([left_eye, right_eye], axis=0)

        return skin_color, eye_color, image, face, face_mask

    def calculate_season(self, skin_color, eye_color):
        distances = {}
        for season, colors in self.seasons.items():
            skin_distances = [euclidean_distances([skin_color], [s])[0][0] for s in colors["skin"]]
            eye_distances = [euclidean_distances([eye_color], [e])[0][0] for e in colors["eye"]]
            distances[season] = self.skin_weight * min(skin_distances) + self.eye_weight * min(eye_distances)
        return min(distances, key=distances.get)

    def calculate_season_probabilities(self, skin_color, eye_color):
        distances = {}
        for season, colors in self.seasons.items():
            skin_distances = [euclidean_distances([skin_color], [s])[0][0] for s in colors["skin"]]
            eye_distances = [euclidean_distances([eye_color], [e])[0][0] for e in colors["eye"]]
            distances[season] = self.skin_weight * min(skin_distances) + self.eye_weight * min(eye_distances)
        total = sum(1/d for d in distances.values())
        probabilities = {season: (1/d)/total * 100 for season, d in distances.items()}
        return probabilities

    def analyze(self, image):
        skin_color, eye_color, image, face, face_mask = self.extract_colors(image)
        season = self.calculate_season(skin_color, eye_color)
        probabilities = self.calculate_season_probabilities(skin_color, eye_color)
        return season, probabilities, skin_color, eye_color, image, face, face_mask
