from django.urls import path, re_path
from . import views
from django.conf.urls import url,include
from rest_framework import routers
from django.conf.urls.static import static
from django.conf import settings


router = routers.DefaultRouter()
router.register('userinfo', views.UserInfoViewSet)
router.register('retaurantinfo', views.RestaurantInfoViewSet)
router.register('ordermenu', views.OrderMenuViewSet)
router.register('order', views.OrderViewSet)
router.register('menuinfo', views.MenuInfoViewSet)


urlpatterns = [
    path('signup/', views.signup),

    re_path(r'^mypage/$', views.mypage_get),
    path('mypage/p', views.mypage_put),

    re_path(r'^myrestaurant/$', views.myrestaurant_get_delete),
    path('myrestaurant/p', views.myrestaurant_post),

    re_path(r'^mymenu/$', views.mymenu_get_delete),
    path('mymenu/p', views.mymenu_post_put),

    # path('user/<pk>/verify/<token>/', UserVerificationView.as_view()),
    path('active/<token>', views.user_active),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^', include(router.urls)),
]   + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)