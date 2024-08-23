from django.urls import path

from personalcol import views

urlpatterns=[
    path('cuttingImage', views.cuttingImage),
    path('detect_mask', views.detect_mask),
    path('seasontone', views.season_tone),
    path('PersonalPredict', views.personal_predict),
    path('base64toFile', views.base64toFile),
]
