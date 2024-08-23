<template>
  <div class="container">
    <div class="w3-container w3-light-grey">
      <h2 class="personal-title">퍼스널컬러 진단</h2>
    </div>
    <input
      type="file"
      ref="fileInput"
      class="ai-form-control mt-1"
      accept="image/*"
      @change="uploadImage"
      style="display: none"
    />
    <div class="personal-flex-container">
      <div
        class="personal-image"
        v-show="!imageUploaded"
        @click="triggerFileInput"
      >
        <img src="img/MakeUp_image/upload3.png" alt="기본 이미지" />
      </div>
      <div
        class="personal-image"
        v-show="imageUploaded"
        @click="triggerFileInput"
      >
        <img :src="uploadedImageUrl" alt="업로드된 이미지" />
      </div>

      <div class="personal-palette">
        <table>
          <thead>
            <tr>
              <th colspan="2">WARM TONE</th>
              <th colspan="2">COOL TONE</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td class="tone-title" colspan="2">SPRING</td>
              <td class="tone-title" colspan="2">SUMMER</td>
            </tr>
            <tr>
              <td colspan="2" class="spring_warm">
                <div
                  class="personal-switch"
                  v-for="(color, index) in springWarmColors"
                  :key="index"
                  :style="{ backgroundColor: color }"
                ></div>
              </td>
              <td colspan="2" class="summer_cool">
                <div
                  class="personal-switch"
                  v-for="(color, index) in summerCoolColors"
                  :key="index"
                  :style="{ backgroundColor: color }"
                ></div>
              </td>
            </tr>
            <tr>
              <td class="tone-title" colspan="2">AUTUMN</td>
              <td class="tone-title" colspan="2">WINTER</td>
            </tr>
            <tr>
              <td colspan="2" class="autumn_warm">
                <div
                  class="personal-switch"
                  v-for="(color, index) in autumnWarmColors"
                  :key="index"
                  :style="{ backgroundColor: color }"
                ></div>
              </td>
              <td colspan="2" class="winter_cool">
                <div
                  class="personal-switch"
                  v-for="(color, index) in winterCoolColors"
                  :key="index"
                  :style="{ backgroundColor: color }"
                ></div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="personal-btn-grp">
      <button
        class="btn btn-next personal-btn"
        @click="movepage"
        style="width: 240px"
      >
        퍼스널컬러 진단
      </button>
    </div>
  </div>
</template>
<script>
import axios from "axios";
export default {
  data() {
    return {
      imageUploaded: false,
      uploadedImageUrl: "",
      maskmessage: "",
      loadingImage: "img/MakeUp_image/loading.gif",

      selectedColor: "#FFFFFF",
      springWarmColors: [
        "#FE9901",
        "#FE804D",
        "#FE592B",
        "#F91D41",
        "#29AAAF",
        "#860EC5",
        "#B0DF73",
        "#8DCD76",
        "#67BD64",
        "#B3E1EE",
        "#67C2B8",
        "#41B198",
        "#EE657B",
        "#EB89B8",
        "#F1E2E5",
        "#F2EED1",
        "#F2ED89",
        "#F1EB0F",
        "#F64490",
        "#D8CB97",
        "#D8B346",
        "#C0AC6F",
        "#4D1A05",
        "#0A286E",
      ],
      summerCoolColors: [
        "#F95C93",
        "#B2D5E8",
        "#9AC4DD",
        "#8E8BC1",
        "#8B56A6",
        "#CCC3D9",
        "#F1D5E9",
        "#EDA4D1",
        "#76A3CC",
        "#5184BB",
        "#535FA9",
        "#6946A0",
        "#D9F0F6",
        "#B3E1CC",
        "#80CCBD",
        "#EA417F",
        "#E5E6AA",
        "#BEA1A6",
        "#A3BBBB",
        "#80A38F",
        "#7E8592",
        "#816F5B",
        "#805042",
        "#6E3738",
      ],
      autumnWarmColors: [
        "#412A0A",
        "#58360B",
        "#F87E78",
        "#FFF2DF",
        "#D8D9C9",
        "#CABC90",
        "#BE8840",
        "#745F10",
        "#757F4D",
        "#4D5E2A",
        "#253F12",
        "#358025",
        "#0C3E18",
        "#35001A",
        "#591C19",
        "#CCA107",
        "#CB7E0F",
        "#CB5003",
        "#C01600",
        "#F7544D",
        "#009377",
        "#80AEA3",
        "#013133",
        "#690B9D",
      ],
      winterCoolColors: [
        "#051536",
        "#0B286E",
        "#75C1DA",
        "#F1FAFD",
        "#F1FEAE",
        "#BEC4DE",
        "#2497DA",
        "#0000FE",
        "#29007B",
        "#850DC4",
        "#FDF200",
        "#0C8731",
        "#0E906C",
        "#005221",
        "#76003A",
        "#760C6E",
        "#D2003F",
        "#FE00FE",
        "#D69ECD",
        "#B1AAD3",
        "#CBCBCB",
        "#808080",
        "#4C4C4C",
        "#030119",
      ],
      season: null,
      spring: null,
      winter: null,
      autumn: null,
      summer: null,
    };
  },
  methods: {
    openFileInput() {
      this.$refs.fileInput.click();
    },
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.imagePreview = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    selectColor(color) {
      this.selectedColor = color;
    },
    triggerFileInput() {
      this.$refs.fileInput.click(); // 파일 업로드 input 클릭
    },
    async uploadImage(event) {
      const file = event.target.files[0];
      if (file) {
        this.uploadedImageUrl = this.loadingImage;
        this.imageUploaded = true;

        const reader = new FileReader();
        reader.onload = (e) => {
          localStorage.setItem("befimg", e.target.result);
          localStorage.setItem("befimgn", file.name);
        };
        reader.readAsDataURL(file);

        const formData = new FormData();
        formData.append("imgfile", file);

        axios
          .post(
            `${process.env.VUE_APP_DJANGO_APP_BACK_END_URL}personalcol/detect_mask`,
            formData,
            {
              headers: { "Content-Type": "multipart/form-data" },
            }
          )
          .then((response) => {
            console.log("전송시작!");
            if (response.data.image) {
              const imageUrl = `data:image/jpeg;base64,${response.data.image}`;
              // console.log('imageUrl',imageUrl)
              localStorage.setItem("befimg", imageUrl);
              this.uploadedImageUrl = imageUrl;

              this.imageUploaded = true;
              this.maskmessage = response.data.message;
            } else {
              alert(response.data.message);
              this.imageUploaded = false;
            }
            console.log("전송완료!");

            axios
              .post(
                `${process.env.VUE_APP_DJANGO_APP_BACK_END_URL}personalcol/seasontone`,
                formData,
                {
                  headers: { "Content-Type": "multipart/form-data" },
                }
              )
              .then((res) => {
                console.log(res.data);
                this.season = res.data.season;
                this.spring = res.data.Spring.toFixed(2);
                this.summer = res.data.Summer.toFixed(2);
                this.autumn = res.data.Autumn.toFixed(2);
                this.winter = res.data.Winter.toFixed(2);
              })
              .catch((error) => {
                console.error("장고 서버로 컬러 요청 실패:", error);
                alert("이미지 전송 중 오류가 발생했습니다.");
              });
          })
          .catch((error) => {
            console.error("장고 서버로 이미지 전송 실패:", error);
            alert("이미지 전송 중 오류가 발생했습니다.");
            this.imageUploaded = false;
          });
      } else {
        alert("이미지를 선택해 주세요.");
      }
    },
    getSeasonNumber(season) {
      if (season === "Spring") {
        return 1;
      } else if (season === "Summer") {
        return 2;
      } else if (season === "Autumn") {
        return 3;
      } else if (season === "Winter") {
        return 4;
      } else {
        return -1; // 알 수 없는 계절 값에 대한 기본 반환 값
      }
    },
    async movepage() {
      const updateform = new FormData();
      updateform.append("memno", localStorage.getItem("memno"));
      updateform.append("seasoncd", this.getSeasonNumber(this.season));
      console.log("계절은" + this.season);
      await axios.post(
        `${process.env.VUE_APP_BACK_END_URL}/personal/seasonUpdate`,
        updateform,
        { headers: { "Content-Type": "application/json" } }
      );
      const list = {
        season: this.season,
        spring: this.spring,
        summer: this.summer,
        autumn: this.autumn,
        winter: this.winter,
      };
      console.log(list);

      this.$router.push({ name: "PersonalCol_Result", query: list });
    },
  },
};
</script>
