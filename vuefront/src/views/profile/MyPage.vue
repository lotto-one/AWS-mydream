<template>
  <div class="container">
    <div class="ProfileTitle">
      <blockquote class="blockquote-profile">
        <b> <p>내 정보</p></b>
      </blockquote>
    </div>
    <div class="user-container">
      <div class="user-profile-header">
        <div
          class="profile-image-container"
          @click="triggerFileInput"
          style="
            margin: 0;
            padding: 0;
            position: relative;
            display: inline-block;
          "
        >
          <img
            class="profile-image"
            :src="`http://localhost/mydream/${myprofile.imgname}`"
            :alt="myprofile.imgname"
            style="display: block"
            @error="handleImageError"
          />
          <!-- <i class="bi bi-camera power-icon"></i> -->
          <input
            type="file"
            ref="fileInput"
            style="display: none"
            @change="handleFileChange"
            accept="image/*"
          />
        </div>
        <div class="header-info">
          <div>
            <h1 class="name">
              {{ myprofile.name }}
              <span class="gender">({{ myprofile.gendercd }})</span>
            </h1>
            <h2 class="title">{{ myprofile.nickname }}</h2>
          </div>
        </div>
        <div style="margin-right: 50px">
          <button class="edit-user-profile-button" @click="handleEditClick">
            프로필 수정
          </button>
          <button
            class="edit-user-profile-button"
            @click="openChangePasswordModal"
            style="margin-top: 15px"
          >
            비밀번호 변경
          </button>
          <button
            class="edit-user-profile-button2"
            style="margin-top: 15px"
            @click="deleteMember"
          >
            회원탈퇴
          </button>
          <ChangePasswordModal
            :visible="isModalVisible"
            @update:visible="isModalVisible = $event"
            @change-password="handlePasswordChange"
          />
        </div>
      </div>

      <div class="user-profile-tab-container">
        <button
          :class="['tab', { active: activeTab === 'info' }]"
          @click="activeTab = 'info'"
        >
          기본 정보
        </button>
      </div>

      <section v-if="activeTab === 'info'">
        <div class="user-profile-section">
          <h3 class="user-profile-section-title">기본 정보</h3>
          <div class="info-grid">
            <div class="info-item">
              <span class="info-icon">👔 지원 분야:</span>
              <span class="info-text">{{ myprofile.categcdText }}</span>
            </div>
            <div class="info-item">
              <span class="info-icon">✉️ 이메일:</span>
              <span class="info-text">{{ myprofile.email }}</span>
            </div>
            <div class="info-item">
              <span class="info-icon">📞 휴대폰 번호:</span>
              <span class="info-text">{{ myprofile.phonenum }}</span>
            </div>
            <div class="info-item">
              <span class="info-icon">🎂 생년월일: </span>
              <span class="info-text">{{ myprofile.birthymd }}</span>
            </div>
            <div class="info-item">
              <span class="info-icon">📍 거주지역: </span>
              <span class="info-text">{{ myprofile.loccdText }}</span>
            </div>
            <div class="info-item">
              <span class="info-icon">🎨 퍼스널 컬러: </span>
              <span class="info-text">{{ myprofile.seasoncd }}</span>
            </div>
          </div>
        </div>
      </section>
    </div>

    <LoginModal
      ref="loginModal"
      :title="loginTitle"
      :message="loginMessage"
      @close="handleLoginModalClose"
    />

    <div :class="['user-profile-modal', { open: isModalOpen }]">
      <div class="modal-content">
        <div class="modal-header">
          <h2>프로필 수정</h2>
          <button @click="handleCloseModal">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-group">
              <label for="name">🍀 이름</label>
              <input
                type="text"
                id="name"
                name="name"
                v-model="tempProfile.name"
                disabled
              />
            </div>
            <div class="form-group">
              <label for="nickname">❤️ 닉네임</label>
              <input
                type="text"
                id="nickname"
                name="nickname"
                v-model="tempProfile.nickname"
              />
            </div>
            <div class="form-group">
              <label for="title">👔 지원 분야</label>
              <select id="title" name="title" v-model="categcdString">
                <option value="1">IT/개발</option>
                <option value="2">교육</option>
                <option value="3">영업/마케팅</option>
                <option value="4">기획/전략</option>
                <option value="5">경영</option>
              </select>
            </div>

            <div class="form-group">
              <label for="email">✉️ 이메일</label>
              <input
                type="email"
                id="email"
                name="email"
                v-model="tempProfile.email"
                disabled
              />
            </div>
            <div class="form-group">
              <label for="phone">📞 휴대폰 번호</label>
              <input
                type="tel"
                id="phone"
                name="phone"
                v-model="tempProfile.phonenum"
              />
            </div>
            <div class="form-group">
              <label for="birthYear">🎂 생년월일</label>
              <input
                type="text"
                id="birthYear"
                name="birthYear"
                v-model="tempProfile.birthymd"
                disabled
              />
            </div>
            <div class="form-group">
              <label for="area">📍 거주 지역</label>
              <select id="area" name="area" v-model="loccdString">
                <option value="1">서울</option>
                <option value="2">경기도</option>
                <option value="3">충청도</option>
                <option value="4">전라도</option>
                <option value="5">경상도</option>
                <option value="6">강원도</option>
                <option value="7">제주도</option>
              </select>
            </div>
            <div class="form-group">
              <label for="area">🎨 퍼스널컬러</label>
              <input
                type="text"
                id="area"
                name="area"
                v-model="tempProfile.season"
                disabled
              />
            </div>
            <div class="modal-footer">
              <button type="submit" class="user-profile-button">저장</button>
              <button
                type="button"
                class="cancel-user-profile-button"
                @click="handleCloseModal"
              >
                취소
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { mapActions } from "vuex";
import ChangePasswordModal from "../../components/ChangePasswordModal.vue";
import LoginModal from "../../components/LoginModal.vue";

export default {
  components: {
    ChangePasswordModal,
    LoginModal,
  },
  data() {
    return {
      memno: localStorage.getItem("memno"),
      // memno: 150,
      activeTab: "info",
      isModalOpen: false,
      myprofile: {},
      myedu: {},
      tempProfile: {},
      loccdMapping: {
        1: "서울",
        2: "경기도",
        3: "충청도",
        4: "전라도",
        5: "경상도",
        6: "강원도",
        7: "제주도",
      },
      categcdMapping: {
        1: "IT/개발",
        2: "교육",
        3: "영업/마케팅",
        4: "기획/전략",
        5: "경영",
      },
      profileImageSrc: "",
      isModalVisible: false,
      loginTitle: "",
      loginMessage: "",
    };
  },
  mounted() {
    this.fetchMemberDetails();
  },
  computed: {
    categcdString: {
      get() {
        return String(this.tempProfile.categcd);
      },
      set(value) {
        this.tempProfile.categcd = Number(value);
      },
    },
    loccdString: {
      get() {
        return String(this.tempProfile.loccd);
      },
      set(value) {
        this.tempProfile.loccd = Number(value);
      },
    },
  },
  watch: {
    profileImageSrc(newVal) {
      // profileImageSrc가 변경될 때 자동으로 처리할 로직을 여기에 추가할 수 있습니다.
      console.log(`Profile image updated to: ${newVal}`);
    },
  },

  methods: {
    deleteMember() {
      const memno = localStorage.getItem("memno");
      const url = `${process.env.VUE_APP_BACK_END_URL}/membership/deleteMember?memno=${memno}`;

      // 사용자에게 삭제 확인 대화상자 표시
      const confirmDelete = window.confirm("정말 삭제하시겠습니까?");

      if (confirmDelete) {
        axios
          .delete(url)
          .then((response) => {
            console.log(response.data);
            alert("회원탈퇴 완료");
            localStorage.clear();
            // 라우터 이동
            this.$router.push("/");
          })
          .catch((err) => {
            console.log(err);
            alert("회원탈퇴 중 오류가 발생했습니다.");
          });
      }
    },
    handleImageError(event) {
      event.target.src = "img/MainPage_image/noimg.png";
    },
    openChangePasswordModal() {
      // console.log(memno);
      console.log(this.currentPassword);
      console.log(this.newPassword);
      this.isModalVisible = true;
    },
    handleModalClose() {
      this.isModalVisible = false;
    },
    async handlePasswordChange({ currentPassword, newPassword }) {
      console.log("Current Password:", currentPassword);
      console.log("New Password:", newPassword);
      try {
        const memno = parseInt(localStorage.getItem("memno"), 10);
        const response = await axios.put(
          `${process.env.VUE_APP_BACK_END_URL}/membership/changePassword`,
          {
            memno,
            currentPassword,
            newPassword,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        this.loginTitle = "";
        this.loginMessage = "비밀번호가 변경되었습니다.";
        this.$refs.loginModal.show();
        this.isModalVisible = false;
      } catch (error) {
        console.error("비밀번호 변경 오류:", error.response.data);
        this.loginTitle = "";
        this.loginMessage = "비밀번호 변경실패";
        this.$refs.loginModal.show();
      }
    },

    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.uploadImage(file);
      }
    },
    ...mapActions(["updateProfileImageSrc"]),
    async uploadImage(file) {
      const formData = new FormData();
      formData.append("file", file);
      formData.append("memno", this.memno);

      try {
        const response = await axios.post(
          `${process.env.VUE_APP_BACK_END_URL}/membership/profileImage`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );

        if (response.status === 200) {
          const imgName = response.data;
          const timestamp = new Date().getTime();

          const newImageUrl = `img/upimg/${imgName}?t=${timestamp}`;
          this.updateProfileImageSrc(newImageUrl);
          window.location.reload();
        }
      } catch (error) {
        console.error("Error uploading image:", error);
        this.loginTitle = "";
        this.loginMessage = "사진 업로드 실패";
        this.$refs.loginModal.show();
      }
    },

    fetchMemberDetails() {
      axios
        .get(
          `${process.env.VUE_APP_BACK_END_URL}/membership/profile?memno=${this.memno}`
        )
        .then((response) => {
          const data = response.data;
          this.myprofile = {
            ...data,
            loccdText: this.convertLoccd(data.loccd),
            categcdText: this.convertCategcd(data.categcd),
          };
          this.tempProfile = { ...this.myprofile };
          console.log(response.data);
        })
        .catch((error) => {
          console.error("에러발생 에러발생");
        });
    },

    convertLoccd(loccd) {
      return this.loccdMapping[loccd] || "알 수 없음";
    },

    convertCategcd(categcd) {
      return this.categcdMapping[categcd] || "알 수 없음";
    },

    handleEditClick() {
      this.tempProfile = { ...this.myprofile };
      this.isModalOpen = true;
    },
    handleCloseModal() {
      this.isModalOpen = false;
    },

    handleSubmit() {
      const dataToSend = { ...this.tempProfile };

      axios
        .put(
          `${process.env.VUE_APP_BACK_END_URL}/membership/profile?memno=${this.memno}`,
          dataToSend
        )
        .then((response) => {
          console.log("Profile updated successfully:", response.data);
          // alert("프로필이 성공적으로 업데이트되었습니다.");
          this.loginTitle = "";
          this.loginMessage = "프로필이 업데이트되었습니다.";
          this.$refs.loginModal.show();

          this.myprofile = {
            ...this.tempProfile,
            loccdText: this.convertLoccd(this.tempProfile.loccd),
            categcdText: this.convertCategcd(this.tempProfile.categcd),
          };
        })
        .catch((error) => {
          console.error("Error updating profile:", error);
          this.loginTitle = "";
          this.loginMessage = "오류가 발생했습니다.";
          this.$refs.loginModal.show();
        });
      this.isModalOpen = false;
    },
  },
};
</script>
<style>
.container {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px 20px;
  font-family: "NanumSquare", sans-serif;
  background-color: #f8f9fa;
  border-radius: 5px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.blockquote-profile {
  display: block;
  background: #fff;
  padding: 20px 20px 10px 45px;
  margin: 0% 5%;
  position: relative;

  /*Font*/
  font-size: 0.35em;
  line-height: 1.5;
  color: #11045e;

  /*Box Shadow - (Optional)*/
  -moz-box-shadow: 2px 2px 15px #ccc;
  -webkit-box-shadow: 2px 2px 15px #ccc;
  box-shadow: 2px 2px 15px #ccc;

  /*Borders - (Optional)*/
  border-left-style: solid;
  border-left-width: 15px;
  /* border-right-style: solid; */
  border-right-width: 0px;
}

.ProfileTitle {
  width: 100%;
  margin: auto;
  margin-bottom: 4%;
  text-align: left;
  color: #ffffff;
  font-size: 5em;
}

.user-profile-header {
  width: 90%;
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.profile-image {
  width: 180px;
  height: 180px;
  border-radius: 50%;
  object-fit: cover;
  margin-left: 40px;
  margin-right: 30px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.header-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 610px;
}

.name {
  font-size: 1.5rem;
  font-weight: bold;
  color: #2c3e50;
  margin: 0 0 5px 0;
}
#name {
  margin-left: 0;
}
#nickname {
  margin-left: 0;
}
#email {
  margin-left: 0;
}

.gender {
  font-size: 1.2rem;
  color: #7f8c8d;
}

.title {
  font-size: 1.2rem;
  font-weight: bold;
  color: #9da2a8;
  margin: 0;
}

.user-profile-tab-container {
  width: 90%;
  display: flex;
  margin-bottom: 30px;
  border-bottom: 2px solid #e0e0e0;
  /* width: 860px; */
}

.tab {
  padding: 10px 20px;
  font-size: 1.2rem;
  font-weight: 600;
  background: none;
  border: none;
  color: #7f8c8d;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab.active {
  color: #1659de;
  font-weight: 900;
  border-bottom: 5px solid #003d8c;
}

.tab:hover {
  color: #3498db;
}

section {
  width: 90%;
}

.user-profile-section {
  background-color: white;
  padding: 25px;
  margin-bottom: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  /* width: 810px; */
}

.user-profile-section-title {
  font-size: 1.1rem;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #ecf0f1;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-icon {
  font-size: 1.1rem;
  margin-right: 10px;
}

.info-text {
  font-size: 1.1rem;
  color: #34495e;
}

.tag-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.tag {
  background-color: #1659de;
  color: #ffffff;
  padding: 5px 10px;
  border-radius: 8px;
}

.education-item {
  display: flex;
  margin-bottom: 20px;
}

.year {
  font-weight: bold;
  margin-right: 20px;
  min-width: 60px;
  font-size: 1.1rem;
}

.details {
  flex: 1;
  text-align: left;
}

.school {
  font-weight: bold;
  margin-bottom: 5px;
  font-size: 1.1rem;
  text-align: left;
}

.degree {
  color: #7f8c8d;
  text-align: left;
}

.introduce {
  font-size: 1.1rem;
  line-height: 1.6;
  color: #34495e;
  text-align: left;
}

.edit-user-profile-button2 {
  margin-left: 50%;
  padding: 12px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  width: 180px;
  height: 50px;
  font-size: 1.1rem;
  background-color: #c50c0c;
  color: #fff;
  transition:
    background-color 0.5s,
    color 0.5s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}
.edit-user-profile-button2:hover {
  background-color: #151414;
  color: #ffffff;
}

.edit-user-profile-button {
  margin-left: 50%;
  padding: 12px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  width: 180px;
  height: 50px;
  font-size: 1.1rem;
  background-color: #102669;
  color: #fff;
  transition:
    background-color 0.5s,
    color 0.5s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.edit-user-profile-button:hover {
  background-color: #ffffff;
  color: #102669;
}

.user-profile-modal {
  display: none;
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.5);
  justify-content: center;
  align-items: center;
}

.user-profile-modal.open {
  display: flex;
}

.modal-content {
  background-color: #fefefe;
  padding: 20px;
  border-radius: 12px;
  width: 80%;
  max-width: 700px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.modal-header button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #7f8c8d;
}

.modal-header h2 {
  color: #2c3e50;
  text-align: center;
  font-weight: 600;
}

.modal-header user-profile-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #7f8c8d;
}

.modal-body {
  margin-bottom: 20px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
}

.form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 10px;
  display: flex;
}

.form-group label {
  display: flex;
  margin-bottom: 5px;
  font-weight: bold;
  color: #2c3e50;
  flex: 5 0 30%;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
}

.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}
.form-group textarea {
  width: 100%;
}

.user-profile-button {
  background-color: #102669;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-left: 10px;
}

.user-profile-button:hover {
  background-color: #003d8c;
}

.cancel-user-profile-button {
  background-color: #6c757d;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-left: 10px;
}

.cancel-user-profile-button:hover {
  background-color: #8c0000;
}

.power-icon {
  position: absolute;
  bottom: -24px; /* 하단에서 20px 떨어진 위치 */
  left: 190px; /* 오른쪽에서 20px 떨어진 위치 */
  font-size: 43px; /* 아이콘 크기 조정 */
  cursor: pointer; /* 클릭 가능한 표시 */
}

.power-icon {
  color: #11045e !important; /* 아이콘 색상 (빨간색) */
}
</style>
