from django.contrib.auth.models import User,Group
from rest_framework import  serializers

from myapp.models import UserInfo, RestaurantInfo, Order, MenuInfo, OrderMenu

class UserInfoSerializer(serializers.HyperlinkedModelSerializer):
    
    class Meta:
        model = UserInfo
        fields = ('id', 'password', 'last_login', 'username', 'first_name', 'last_name', 'email', 'is_staff', 'is_active', 'date_joined', 'is_owner')

class RestaurantInfoSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = RestaurantInfo
        fields = ('owner','owner_id','restaurant_name','restaurant_address', 'restaurant_latitude','restaurant_longitude','restaurant_image')

class OrderSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Order
        fields = ('user','order_id', 'order_time', 'tot_price','table_id')

class MenuInfoSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = MenuInfo
        fields = ('restaurant','restaurant_id','menu_id', 'menu_name', 'menu_price','menu_desc', 'menu_image')
        
class OrderMenuSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = OrderMenu
        fields = ('order', 'menu', 'menu_tot_price', 'menu_num')
