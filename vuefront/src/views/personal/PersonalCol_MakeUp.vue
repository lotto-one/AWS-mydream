<template>
  <div class="container">
    <div class="w3-container w3-light-grey">
      <h2 class="personal-title">
        MakeUp Yourself
        <i
          class="bi bi-info-circle"
          style="font-size: 0.7em"
          data-bs-toggle="popover"
          title="메이크업을 해보세요"
          data-bs-content="하단의 MakUp 이미지를 클릭하여 피부톤에 맞는 화장 전 후를 비교해 보세요."
        ></i>
      </h2>
    </div>
    <div class="personal-flex-container">
      <div class="personal-makeup-img">
        <p>Before MakeUp</p>
        <img :src="beforeImage" />
      </div>
      <div class="personal-makeup-img">
        <p>After MakeUp</p>
        <img :src="afterImage" />
      </div>
    </div>
    <!-- <div class="personal-btn-grp"> -->
    <button type="button" class="btn btn-next personal-btn" @click="proupdate">
      프로필 사진 업데이트
    </button>
    <!-- </div> -->

    <div id="personal-target" class="personal-makeupSlide">
      <swiper
        :modules="modules"
        :slides-per-view="3"
        :space-between="50"
        navigation
        :pagination="{ clickable: true, el: '.swiper-pagination-custom' }"
        :scrollbar="{ draggable: true }"
        :loop="true"
        @swiper="onSwiper"
        @slideChange="onSlideChange"
        style="padding: 20px"
      >
        <swiper-slide v-for="index in 4" :key="index">
          <p style="font-size: 20px; font-weight: bold">
            {{ translatedSeason }} {{ translatedGender }} MakeUp {{ index }}
          </p>
          <br />
          <img
            :src="getMakeupImage(index)"
            class="personal-target-img"
            @click="afterMakeup(getMakeupImage(index))"
          />
        </swiper-slide>
        <div class="swiper-pagination-custom"></div>
        <!-- Pagination을 위한 요소 -->
      </swiper>
    </div>
    <MUsave
      v-if="savecom"
      @close="closeModal_personal"
      v-bind:mData="this.modalData"
    />
  </div>
</template>

<script>
import { useRoute } from "vue-router";
import { Navigation, Pagination, A11y } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/vue";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import { Popover } from "bootstrap"; // Bootstrap에서 Popover 가져오기
import axios from "axios";
import MUsave from "../../components/MakeUpSave.vue";
export default {
  data() {
    return {
      files: [],
      croppedImageSrc: localStorage.getItem("croppedImage"),
      modules: [Navigation, Pagination, A11y],
      season: "summer",
      mgender: "남",
      imgpath: "img/before_image/",
      beforeImage: localStorage.getItem("uploadedImage"),
      afterImage: localStorage.getItem("uploadedImage"),
      loadingImage: "img/MakeUp_image/loading.gif",
      savecom: false,
      memno: localStorage.getItem("memno"),
    };
  },
  setup() {
    // 하이차트에 들어갈 값
    // useRoute 훅을 사용하여 현재 라우트 객체를 가져옵니다.
    const route = useRoute();
    console.log(route.query);
    return {
      season: route.query.season,
    };
  },

  computed: {
    translatedSeason() {
      const seasonMap = {
        spring: "봄",
        summer: "여름",
        fall: "가을",
        winter: "겨울",
      };
      return seasonMap[this.season] || this.season;
    },
    translatedGender() {
      const genderMap = {
        남성: "남자",
        여성: "여자",
      };
      return genderMap[this.mgender] || this.mgender;
    },
  },
  methods: {
    genderset() {
      const num = new FormData();
      num.append("memno", this.memno);
      axios
        .post(`${process.env.VUE_APP_BACK_END_URL}/personal/genget`, num, {
          headers: { "Content-Type": "application/json" },
        })
        .then((res) => {
          console.log("gender : " + res.data);
          this.mgender = res.data;
        });
    },
    closeModal_personal() {
      this.savecom = !this.savecom;
    },
    getMakeupImage(index) {
      const genderMap = {
        남성: "남",
        여성: "여",
      };

      return `img/MakeUp_image/${genderMap[this.mgender]}_${this.season}_${index}.png`;
    },
    async proupdate() {
      try {
        const afimgn = ".jpg";
        const afterImage = await this.loadImageAsBase64(this.afterImage);
        const formData = new FormData();
        formData.append("afterimage", this.dataURItoBlob(afterImage), afimgn);

        const response = await axios.post(
          `${process.env.VUE_APP_DJANGO_APP_BACK_END_URL}personalcol/base64toFile`,
          formData,
          {
            headers: { "Content-Type": "multipart/form-data" },
            responseType: "blob",
          }
        );
        console.log(response.data);
        const springformData = new FormData();
        console.log(afimgn);
        springformData.append("imgfile", response.data, afimgn);
        const usernum = new FormData();
        usernum.append("memno", this.memno);
        await axios
          .post(
            `${process.env.VUE_APP_BACK_END_URL}/personal/makeUpImgSave`,
            springformData,
            { headers: { "Content-Type": "multipart/form-data" } }
          )
          .then((res) => {
            console.log("imgname : " + res.data);
            usernum.append("imgname : ", res.data);
            axios
              .post(
                `${process.env.VUE_APP_BACK_END_URL}/personal/makeUpImgupdate`,
                usernum,
                {
                  headers: { "Content-Type": "application/json" },
                }
              )
              .then((res) => {
                console.log(this.savecom);
                this.savecom = !this.savecom;
              });
          });
      } catch (error) {
        console.error("Error updating history:", error);
      }
    },
    async afterMakeup(makeupImagePath) {
      try {
        const beforeImage = await this.loadImageAsBase64(this.beforeImage);
        const makeupImage = await this.loadImageAsBase64(makeupImagePath);
        this.afterImage = this.loadingImage;
        const response = await axios.post(
          `${process.env.VUE_APP_DJANGO_APP_BACK_END_URL}personalcol/PersonalPredict`,
          {
            before_image: beforeImage,
            makeup_image: makeupImage,
          }
        );
        const imageData = response.data.image_base64;
        this.afterImage = "data:image/jpeg;base64," + imageData;
      } catch (error) {
        console.error("Error loading makeup image:", error);
      }
    },
    loadImageAsBase64(imagePath) {
      return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
          const reader = new FileReader();
          reader.onloadend = function () {
            resolve(reader.result);
          };
          reader.readAsDataURL(xhr.response);
        };
        xhr.onerror = function () {
          reject(new Error("Failed to load image"));
        };
        xhr.open("GET", imagePath);
        xhr.responseType = "blob";
        xhr.send();
      });
    },
    dataURItoBlob(dataURI) {
      const byteString = atob(dataURI.split(",")[1]);
      const mimeString = dataURI.split(",")[0].split(":")[1].split(";")[0];
      const ab = new ArrayBuffer(byteString.length);
      const ia = new Uint8Array(ab);
      for (let i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
      }
      return new Blob([ab], { type: mimeString });
    },
  },
  mounted() {
    // Bootstrap Popover 초기화
    this.genderset();
    const popoverTriggerList = document.querySelectorAll(
      '[data-bs-toggle="popover"]'
    );
    popoverTriggerList.forEach((popoverTriggerEl) => {
      new Popover(popoverTriggerEl);
    });
  },
  components: {
    Swiper,
    SwiperSlide,
    MUsave,
  },
};
</script>
