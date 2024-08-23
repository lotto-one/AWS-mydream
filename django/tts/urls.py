from django.urls import path
from . import views

urlpatterns = [
    path('text_to_speech/', views.text_to_speech),
]