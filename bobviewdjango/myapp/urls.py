from django.urls import path
from . import views
from django.conf.urls import url,include
from rest_framework import routers
from django.conf.urls.static import static
from django.conf import settings


router = routers.DefaultRouter()
router.register('userinfo', views.UserInfoViewSet)
router.register('restaurantinfo', views.RestaurantInfoViewSet)
router.register('ordermenu', views.OrderMenuViewSet)
router.register('order', views.OrderViewSet)
router.register('menuinfo', views.MenuInfoViewSet)


urlpatterns = [
    path('testing/', views.testing),
    path('signup/add/', views.addSignup),
    path('signup/modify/', views.modifySignup),
    path('login/apply/', views.applyLogin),
    path('login/verify/', views.verifyLogin),
    path('logout/', views.verifyLogin),
    path('mypage/', views.mypage),
    path('myrestaurant/', views.myrestaurant),
    

    # path('user/<pk>/verify/<token>/', UserVerificationView.as_view()),
    path('active/<token>', views.user_active),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^', include(router.urls)),
]   + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)