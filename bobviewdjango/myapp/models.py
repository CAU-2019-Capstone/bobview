# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Make sure each ForeignKey has `on_delete` set to the desired behavior.
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from django.db import models
from django.contrib.auth.models import AbstractUser


class UserInfo(AbstractUser):
    is_owner = models.BooleanField(default=False)
    class Meta:
        managed = True
        db_table = 'user_info'

class ImageTable(models.Model):
    image = models.ImageField(blank=True)
    class Meta:
        managed = True
        db_table = 'image_table'

class RestaurantInfo(models.Model):
    # owner = models.OneToOneField(UserInfo, on_delete=models.CASCADE)          # UserInfo와 일대일 관계
    owner = models.ForeignKey(UserInfo, on_delete=models.CASCADE)               # UserInfo와 다대일 관계
    restaurant_name = models.CharField(primary_key=True, max_length=45, unique=True)
    restaurant_address = models.CharField(max_length=128, unique=True)
    restaurant_latitude = models.FloatField()
    restaurant_longitude = models.FloatField()
    restaurant_image = models.ImageField(blank=True)
    tot_table_num = models.IntegerField()

    class Meta:
        managed = True
        db_table = 'restaurant_info'



class MenuInfo(models.Model):
    restaurant = models.ForeignKey(RestaurantInfo, on_delete=models.CASCADE)    # RestaurantInfo와 다대일 관계
    menu_id = models.AutoField(primary_key=True)
    menu_name = models.CharField(max_length=45)
    menu_price = models.IntegerField()
    menu_desc = models.TextField()
    menu_image = models.ImageField(blank=True)

    class Meta:
        managed = True
        db_table = 'menu_info'


class UserOrder(models.Model):
    user = models.ForeignKey(UserInfo, on_delete=models.CASCADE)                # UserInfo와 다대일 관계
    user_order_id = models.AutoField(primary_key=True)
    restaurant = models.ForeignKey(RestaurantInfo, on_delete=models.CASCADE)    # RestaurantInfo와 다대일 관계
    order_time = models.DateTimeField()
    table_id = models.IntegerField()
    is_active = models.BooleanField(default=False)

    class Meta:
        managed = True
        db_table = 'user_order'
        # unique_together = (('user', 'order_id'),)


class OrderContents(models.Model):
    userorder = models.ForeignKey(UserOrder, on_delete=models.CASCADE)          # UserOrder와 다대일 관계
    menu = models.ForeignKey(MenuInfo, on_delete=models.CASCADE)                # MenuInfo와 다대일 관계
    order_contents_id = models.AutoField(primary_key=True)
    menu_num = models.IntegerField()

    class Meta:
        managed = True
        db_table = 'order_contents'
        # unique_together = (('userorder', 'restaurant', 'order_id'),)


class RestRating(models.Model):
    restaurant = models.ForeignKey(RestaurantInfo, on_delete=models.CASCADE)    # RestaurantInfo와 다대일 관계
    user = models.ForeignKey(UserInfo, on_delete=models.CASCADE)                # UserInfo와 다대일 관계
    rest_rating_id = models.AutoField(primary_key=True)
    rating = models.FloatField()
    desc = models.TextField()
    like = models.IntegerField()

    class Meta:
        managed = True
        db_table = 'rest_rating'
        # unique_together = (('restaurant', 'user', 'rest_rating_id'),)


class MenuRating(models.Model):
    menu = models.ForeignKey(MenuInfo, on_delete=models.CASCADE)                # MenuInfo와 다대일 관계
    user = models.ForeignKey(UserInfo, on_delete=models.CASCADE)                # UserInfo와 다대일 관계
    menu_rating_id = models.AutoField(primary_key=True)
    rating = models.FloatField()
    desc = models.TextField()
    like = models.IntegerField()

    class Meta:
        managed = True
        db_table = 'menu_rating'
        # unique_together = (('menu', 'user', 'menu_rating_id'),)

class CommentList(models.Model):
    comment_list_id = models.AutoField(primary_key=True)
    rest_rating = models.ForeignKey(RestRating, on_delete=models.CASCADE, null=True)         # RestRating와 다대일 관계
    menu_rating = models.ForeignKey(MenuRating, on_delete=models.CASCADE, null=True)         # MenuRating와 다대일 관계
    comment = models.TextField()
    user = models.ForeignKey(UserInfo, on_delete=models.CASCADE)                # UserInfo와 다대일 관계

    class Meta:
        managed = True
        db_table = 'comment_list'

class Messages(models.Model):
    restaurant = models.ForeignKey(RestaurantInfo, on_delete=models.CASCADE)          # UserOrder와 다대일 관계
    table_id = models.IntegerField()
    message = models.TextField()

    class Meta:
        managed = True
        db_table = 'messages'

class MenuTemplate(models.Model):
    restaurant = models.ForeignKey(RestaurantInfo, on_delete=models.CASCADE)    # RestaurantInfo와 다대일 관계
    menu_type = models.IntegerField(default=0)
    font_type = models.IntegerField(default=0)
    font_caption_size = models.IntegerField(default=24)
    font_normal_size = models.IntegerField(default=16)
    font_caption_color = models.IntegerField(default=0)
    fond_normal_color = models.IntegerField(default=10)
    is_active = models.BooleanField(default=False)
    
    class Meta:
        managed = True
        db_table = 'menu_template'

#for cocktail...
#TODO
class Cocktail(models.Model):
    cocktail_id = models.IntegerField(primary_key=True, null=False)
    name = models.CharField(max_length=60)
    alcoholic = models.BooleanField(default=False)
    category = models.CharField(max_length=30)
    glass = models.CharField(max_length=30)
    instruction = models.TextField()
    drink_image = models.CharField(max_length=255)

    class Meta:
        managed = True
        db_table = 'cocktail'

class CocktailInstance(models.Model):
    instance_id = models.AutoField(primary_key=True)
    cocktail_like = models.TextField()

    class Meta:
        managed = True
        db_table = 'cocktail_instance'