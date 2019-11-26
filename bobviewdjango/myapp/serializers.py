from django.contrib.auth.models import User,Group
from rest_framework import  serializers

from .models import *

class UserInfoSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = UserInfo
        fields = ('id', 'password', 'last_login', 'username', 'first_name', 'last_name', 'email', 'is_staff', 'is_active', 'date_joined', 'is_owner')

class RestaurantInfoSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = RestaurantInfo
        fields = ('owner','restaurant_name','restaurant_address', 'restaurant_latitude','restaurant_longitude','restaurant_image', 'tot_table_num')

class ImageTableSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = ImageTable
        fields = "__all__"

class MenuInfoSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = MenuInfo
        fields = ('restaurant', 'menu_id', 'menu_name', 'menu_price','menu_desc', 'menu_image')

class UserOrderSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = UserOrder
        fields = ('user','user_order_id', 'restaurant', 'order_time','table_id', 'is_active')
        
class OrderContentsSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = OrderContents
        fields = ('userorder', 'menu', 'order_contents_id', 'menu_num')

class RestRatingSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = RestRating
        fields = ('restaurant', 'user', 'rest_rating_id', 'rating', 'desc', 'like')

class MenuRatingSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = MenuRating
        fields = ('menu', 'user', 'menu_rating_id', 'rating', 'desc', 'like')

class CommentListSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = CommentList
        fields = ('comment_list_id','rest_rating', 'menu_rating', 'comment', 'user')

class MenuTemplateSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = MenuTemplate
        fields = ('restaurant', 'menu_type', 'font_type', 'font_caption_size', 'font_normal_size', 'font_caption_color', 'fond_normal_color', 'is_active')

class MessagesSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Messages
        fields = ('restaurant', 'table_id', 'message')

class CocktailSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Cocktail
        fields = ('cocktail_id', 'name', 'alcoholic','category','glass','instruction','drink_image')

class CocktailInstanceSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = CocktailInstance
        fields = ('instance_id', 'cocktail_like')
