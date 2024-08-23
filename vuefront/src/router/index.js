import { createRouter, createWebHistory } from "vue-router";
import InterviewRes from "../views/InterviewRes.vue";
import OneToOne from "../views/OneToOne.vue";
import store from "../store"; // Vuex 스토어

const routes = [
  {
    path: "/",
    name: "Landing",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/Landing.vue"),
    meta: { hideLayout: true }, // 메타 필드 추가
  },

  {
    path: "/ResPersonality",
    name: "ResPersonality",

    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/ai/ResPersonalityView.vue"
      ),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/ResDuty",
    name: "ResDuty",

    component: () =>
      import(/* webpackChunkName: "about" */ "../views/ai/ResDutyView.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/ResumeList",
    name: "ResumeList",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/resume/ResumeView.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/ResumeWrite",
    name: "ResumeWrite",

    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/resume/ResumeWriteView.vue"
      ),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/ResumeWrite2",
    name: "ResumeWrite2",

    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/resume/ResumeWriteView.vue"
      ),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/ResumeWrite",
    name: "ResumeUpdate",
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/resume/ResumeWriteView.vue"
      ),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/MyPage",
    name: "MyPage",
    component: () => import("../views/profile/MyPage.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/personalCol_Test",
    name: "PersonalCol_Test",
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/personal/PersonalCol_Test.vue"
      ),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/personal_Result",
    name: "PersonalCol_Result",
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/personal/PersonalCol_Result.vue"
      ),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/personal_MakeUp",
    name: "PersonalCol_MakeUp",
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/personal/PersonalCol_MakeUp.vue"
      ),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/InterviewRes",
    name: "InterviewRes",
    component: InterviewRes,
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/OneToOne",
    name: "OneToOne",
    component: OneToOne,
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/AISetting",
    name: "AISetting",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/ai/AISettingView.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/AIFaceTest",
    name: "AIFaceTest",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/ai/AIFaceTestView.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/AIMicTest",
    name: "AIMicTest",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/ai/AIMicTestView.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/AIInterviewChoice",
    name: "AIInterviewChoice",
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/ai/AIInterviewChoiceView.vue"
      ),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/AIInterview1",
    name: "AIInterview1",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/ai/AIInterview1.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/AIInterview2",
    name: "AIInterview2",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/ai/AIInterview2.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/AIInterview3",
    name: "AIInterview3",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/ai/AIInterview3.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/AIInterview4",
    name: "AIInterview4",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/ai/AIInterview4.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/AIInterview5",
    name: "AIInterview5",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/ai/AIInterview5.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/AIInterview6",
    name: "AIInterview6",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/ai/AIInterview6.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/AIInterview7",
    name: "AIInterview7",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/ai/AIInterview7.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/login",
    name: "login",
    component: () => import("../views/Login.vue"),
    meta: { hideLayout: true }, // 메타 필드 추가
  },
  {
    path: "/ConsultantInfo",
    name: "ConsultantInfo",
    component: () => import("../views/ConsultantInfo.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/Calendar",
    name: "Calendar",
    component: () => import("../views/CalendarView.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/Main",
    name: "Main",
    component: () => import("../views/Main.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/test",
    name: "test",
    component: () => import("../views/TestVue.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
  {
    path: "/MyConsultantProfile",
    name: "MyConsultantProfile",
    component: () => import("../views/MyConsultantProfile.vue"),
    meta: { requiresAuth: true }, // 인증이 필요한 라우트
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

// 전역 가드 설정
router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    // 토큰이 없으면 로그인 페이지로 리다이렉트
    if (!store.getters.isAuthenticated) {
      alert("비정상적인 접근입니다. 로그인해주세요.");
      next({
        path: "/login",
        query: { redirect: to.fullPath }, // 로그인 후 원래 페이지로 리디렉션 가능
      });
    } else {
      next(); // 인증된 경우 정상적으로 이동
    }
  } else {
    next(); // 인증이 필요 없는 경우 정상적으로 이동
  }
});

export default router;
