from django.urls import path
from . import views
from django.conf.urls import url,include
from rest_framework import routers


router = routers.DefaultRouter()
router.register('userinfo', views.UserInfoViewSet)
router.register('retaurantinfo', views.RestaurantInfoViewSet)
router.register('ordermenu', views.OrderMenuViewSet)
router.register('order', views.OrderViewSet)
router.register('menuinfo', views.MenuInfoViewSet)


urlpatterns = [
    path('testing/', views.testing),
    path('dologin/', views.dologin),
    path('signup/', views.signup),
    path('dosignup/', views.dosignup),
    path('mypage/', views.mypage)
    path('myrestaurant', view.myrestaurant)
    

    # path('user/<pk>/verify/<token>/', UserVerificationView.as_view()),
    path('active/<token>', views.user_active),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^', include(router.urls)),
]