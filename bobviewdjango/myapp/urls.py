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


urlpatterns = [

    re_path(r'^mypage/$', views.mypage_get),
    path('mypage/p', views.mypage_put),

    re_path(r'^myrestaurant/$', views.myrestaurant_get_delete),
    path('myrestaurant/p', views.myrestaurant_post),

    re_path(r'^mymenu/$', views.mymenu_get_delete),
    path('mymenu/p', views.mymenu_post_put),
    path('signup/add/', views.addSignup),
    path('signup/modify/', views.modifySignup),
    path('login/apply/', views.applyLogin),
    path('login/verify/', views.verifyLogin),
    

    # path('user/<pk>/verify/<token>/', UserVerificationView.as_view()),
    path('active/<token>', views.user_active),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^', include(router.urls)),
]   + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)