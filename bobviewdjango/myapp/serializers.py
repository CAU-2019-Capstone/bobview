from django.contrib.auth.models import User,Group
from rest_framework import  serializers

from myapp.models import UserInfo, RestaurantInfo, Order, MenuInfo, OrderMenu

class UserInfoSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = UserInfo
        fields = ('id', 'password', 'last_login', 'is_superuser', 'username', 'first_name', 'last_name', 'email', 'is_staff', 'is_active', 'date_joined', 'is_owner')

class RestaurantInfoSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = RestaurantInfo
        fields = ('owner','restaurant_name','restaurant_gps', 'restaurant_rating','restaurant_image')

class OrderSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Order
        fields = ('user','order_id', 'order_time', 'tot_price','table_id')

class MenuInfoSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = MenuInfo
        fields = ('menu_id', 'menu_name', 'menu_price','menu_desc', 'menu_rating', 'menu_image')
        
class OrderMenuSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = OrderMenu
        fields = ('order', 'menu', 'menu_tot_price', 'menu_num')
