from django.contrib.auth.models import User,Group
from rest_framework import  serializers

from .models import *

class UserInfoSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = UserInfo
        fields = ('id', 'password', 'last_login', 'is_superuser', 'username', 'first_name', 'last_name', 'email', 'is_staff', 'is_active', 'date_joined', 'is_owner')

class RestaurantInfoSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = RestaurantInfo
        fields = ('owner','restaurant_name','restaurant_gps', 'restaurant_rating','restaurant_image')

class MenuInfoSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = MenuInfo
        fields = ('menu_id', 'menu_name', 'menu_price','menu_desc', 'menu_rating', 'menu_image')

class UserOrderSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = UserOrder
        fields = ('user','user_order_id', 'order_time', 'tot_price','table_id')
        
class OrderContentsSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = OrderContents
        fields = ('userorder', 'restaurant', 'menu', 'order_contents_id', 'menu_num')

class RestRatingSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = RestRating
        fields = ('restaurant', 'user', 'rest_rating_id', 'rating', 'desc')

class MenuRatingSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = MenuRating
        fields = ('menu', 'user', 'menu_rating_id', 'rating', 'desc')