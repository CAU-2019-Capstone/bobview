from django.urls import path, re_path
from . import views
from django.conf.urls import url,include
from rest_framework import routers
from django.conf.urls.static import static
from django.conf import settings


router = routers.DefaultRouter()
router.register('userinfo', views.UserInfoViewSet)
router.register('restaurantinfo', views.RestaurantInfoViewSet)
router.register('ordercontents', views.OrderContentsViewSet)
router.register('userorder', views.UserOrderViewSet)
router.register('menuinfo', views.MenuInfoViewSet)
router.register('restrating', views.RestRatingViewSet)
router.register('menurating', views.MenuRatingViewSet)
router.register('imagetable', views.ImageTableViewSet)


urlpatterns = [

    path('mypage/p', views.mypage_put),
    path('myrestaurant/p', views.myrestaurant_post),
    path('mymenu/p', views.mymenu_post_put),
    path('signup/add/', views.addSignup),
    path('signup/modify/', views.modifySignup),
    path('login/apply/', views.applyLogin),
    path('login/verify/', views.verifyLogin),
    path('upload/image/', views.postImage),
    path('order/create/', views.createOrder),
    path('order/update/', views.updateOrder),


    # path('user/<pk>/verify/<token>/', UserVerificationView.as_view()),
    path('active/<token>', views.user_active),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^', include(router.urls)),
]   + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)