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
router.register('commentlist', views.CommentListViewSet)
router.register('menutemplate', views.MenuTemplateViewSet)
router.register('messages', views.MessagesViewSet)
router.register('cocktail', views.CocktailViewSet)
router.register('cocktailInstance', views.CocktailInstanceViewSet)

urlpatterns = [
    path('community/main/', views.community_main),     
    path('community/search/', views.search), 
    path('signup/add/', views.addSignup),
    path('signup/modify/', views.modifySignup),
    path('login/apply/', views.applyLogin),
    path('login/verify/', views.verifyLogin),
    path('upload/image/', views.postImage),
    path('order/create/', views.createOrder),
    path('order/change/', views.changeOrder),
    path('order/active/', views.getActiveOrder),
    path('cocktail/recommend/CBF/', views.cocktailRecommentCBF),
    path('cocktail/recommend/CF/', views.cocktailRecommentCF),

    # path('user/<pk>/verify/<token>/', UserVerificationView.as_view()),
    path('active/<token>', views.user_active),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^', include(router.urls)),
]   + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)