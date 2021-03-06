from django.shortcuts import render, redirect
from django.template import loader
# from django import forms
from django.contrib.auth import login, authenticate
from django.http import HttpResponse
from django.contrib.auth.models import User, Group
from django.contrib import messages
from django.contrib.auth.tokens import default_token_generator
from django.core.mail import EmailMessage
from django.shortcuts import get_object_or_404
import random
import datetime
from django.utils import timezone
# trans
from django.views.decorators.csrf import csrf_exempt
from django.http import HttpResponse, JsonResponse
from rest_framework.decorators import api_view
from rest_framework.response import Response
# serializer
from rest_framework import viewsets
from myapp.serializers import *
from myapp.models import *
from rest_framework.parsers import JSONParser

# Create your views here.
# api
class UserInfoViewSet(viewsets.ModelViewSet):
    queryset = UserInfo.objects.all()
    serializer_class = UserInfoSerializer

    def retrieve(self, request, pk=None):
        serializer_context = {
            'request': request,
        }
        user = get_object_or_404(self.queryset, username = pk)
        serializer = UserInfoSerializer(user,context=serializer_context)
        return Response(serializer.data)

    def destroy(self, request, pk=None):
        message = ''
        user = get_object_or_404(self.queryset, username = pk)
        password = self.request.query_params.get('password')
        if user.check_password(password):
            user.delete()
            message = 'success'
        else:
            message = 'fail'
        resp = JsonResponse({
            'message' : message,
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp  

class RestaurantInfoViewSet(viewsets.ModelViewSet):
    queryset = RestaurantInfo.objects.all()
    serializer_class = RestaurantInfoSerializer

    def retrieve(self, request, pk=None, format=None):
        owner = self.request.query_params.get('owner')
        print(pk)
        print(owner)
        serializer_context = {
            'request': request,
        }
        try:
            userqueryset = UserInfo.objects.all()
            user = get_object_or_404(userqueryset, username = owner)
            restaurant = RestaurantInfo.objects.filter(owner = user)
        except:
            restaurant = RestaurantInfo.objects.filter(restaurant_name = pk)
        serializer = RestaurantInfoSerializer(restaurant, many=True, context=serializer_context)
        return Response(serializer.data)
    
    def create(self, request, pk=None, format=None):
        print("create restaurant")
        print(request.data)
        data=request.data
        owner = get_object_or_404(UserInfo, username=data['username'])

        new_restaurant = RestaurantInfo(restaurant_name=data['restaurant_name'], restaurant_address=data['restaurant_address'], 
                                        restaurant_latitude=data['restaurant_latitude'], restaurant_longitude=data['restaurant_longitude'], owner=owner) # 이미지는 로컬에 저장하는거 해야됨.....
        if(request.data['restaurant_image'] is not ''):
            image = get_object_or_404(ImageTable, id = request.data['restaurant_image'])
            new_restaurant.restaurant_image = image.image
        new_restaurant.save()
        
        resp = JsonResponse({
            'message' : 'success'
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp


class MenuInfoViewSet(viewsets.ModelViewSet):
    queryset = MenuInfo.objects.all()
    serializer_class = MenuInfoSerializer

    def retrieve(self, request, pk=None, format=None):
        restaurant_name = self.request.query_params.get('restaurant_name')
        serializer_context = {
            'request': request,
        }
        try:
            restaurantquery = RestaurantInfo.objects.all()
            restaurant = get_object_or_404(restaurantquery, restaurant_name = restaurant_name)
            menus = MenuInfo.objects.filter(restaurant = restaurant)
        except:
            menus = MenuInfo.objects.filter(menu_id=pk)

        serializer = MenuInfoSerializer(menus,many=True,context=serializer_context)
        return Response(serializer.data)

    def create(self, request, pk=None, format=None):
        data=request.data
        print(data)
        restaurant_name = data['restaurant_name']
        restaurant = get_object_or_404(RestaurantInfo, restaurant_name=restaurant_name)
        new_menu = MenuInfo(restaurant=restaurant, menu_name=data['menu_name'], 
                            menu_price = data['menu_price'], menu_desc = data['menu_desc'])
        if(data['menu_image'] is not ''):
            image = get_object_or_404(ImageTable, id = data['menu_image'])
            new_menu.menu_image = image.image
        new_menu.save()
        resp = JsonResponse({
            'message' : 'success',
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp  

    def destroy(self, request, pk=None):
        menu = get_object_or_404(self.queryset, menu_id = pk)
        menu.delete()
        resp = JsonResponse({
            'message' : 'success',
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp  

    def update(self, request, pk=None):
        print(request.data)
        menu = get_object_or_404(self.queryset, menu_id = pk)
        menu.menu_name = request.data['menu_name']
        menu.menu_price = request.data['menu_price']
        menu.menu_desc = request.data['menu_desc']
        if(request.data['menu_image'] is not ''):
            image = get_object_or_404(ImageTable, id = request.data['menu_image'])
            menu.menu_image = image.image
        menu.save()
        resp = JsonResponse({
            'message' : 'success',
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp 

        
class ImageTableViewSet(viewsets.ModelViewSet):
    queryset = ImageTable.objects.all()
    serializer_class = ImageTableSerializer

class UserOrderViewSet(viewsets.ModelViewSet):
    queryset = UserOrder.objects.all()
    serializer_class = UserOrderSerializer
    def retrieve(self, request, pk=None, format=None):
        username = self.request.query_params.get('username')
        isActive = self.request.query_params.get('is_active')
        print(pk)
        print(self.request.query_params)
        try:
            userquery = UserInfo.objects.all()
            user = get_object_or_404(userquery, username=username)
            if isActive is None:
                userOrder = UserOrder.objects.filter(user=user)
            else :
                userOrder = UserOrder.objects.filter(user=user, is_active=True)
        except:
            print("query error")
            userOrder = UserOrder.objects.filter(user_order_id=pk)

        serializer_context = {
            'request': request,
        }
        serializer = UserOrderSerializer(userOrder, many=True, context=serializer_context)
        return Response(serializer.data)

class OrderContentsViewSet(viewsets.ModelViewSet):
    queryset = OrderContents.objects.all()
    serializer_class = OrderContentsSerializer

    def retrieve(self, request, pk=None, format=None):
        order_id = self.request.query_params.get('order_id')
        print(pk)
        print(self.request.query_params)
        try:
            orderquery = UserOrder.objects.all()
            userorder = get_object_or_404(orderquery, user_order_id=order_id)
            orderContents = OrderContents.objects.filter(userorder=userorder)
        except:
            userorder = None
            orderContents = OrderContents.objects.filter(order_contents_id=pk)

        serializer_context = {
            'request': request,
        }
        serializer = OrderContentsSerializer(orderContents, many=True, context=serializer_context)
        return Response(serializer.data)

class RestRatingViewSet(viewsets.ModelViewSet):
    queryset = RestRating.objects.all()
    serializer_class = RestRatingSerializer
    def retrieve(self, request, pk=None, format=None):
        restaurant_name = self.request.query_params.get('restaurant_name')
        username = self.request.query_params.get('username')
        print(pk)
        print(self.request.query_params)
        try:
            restaurantquery = RestaurantInfo.objects.all()
            restaurant = get_object_or_404(restaurantquery, restaurant_name=restaurant_name)
            if username is None:
                restRating = RestRating.objects.filter(restaurant=restaurant)
            else :
                userquery = UserInfo.objects.all()
                user = get_object_or_404(userquery, username=username)
                restRating = RestRating.objects.filter(restaurant=restaurant, user=user)
        except:
            print("query error")
            restRating = RestRating.objects.filter(rest_rating_id=pk)

        serializer_context = {
            'request': request,
        }
        serializer = RestRatingSerializer(restRating, many=True, context=serializer_context)
        return Response(serializer.data)

    def create(self,request, pk=None, format=None):
        print(request.data)
        restaurantquery = RestaurantInfo.objects.all()
        restaurant = get_object_or_404(restaurantquery, restaurant_name=request.data['restaurant_name'])
        userinfoquery = UserInfo.objects.all()
        user = get_object_or_404(userinfoquery,username=request.data['username'])
        newRestReview = RestRating(restaurant= restaurant, user=user, rating=request.data['rating'], desc=request.data['desc'], like=0)
        newRestReview.save()
        resp = JsonResponse({
            'message' : 'success',
        })
        return resp
    def update(self,request, pk=None, format=None):
        print(pk)
        print(request.data)
        restratingquery = RestRating.objects.all()
        currentRating = get_object_or_404(restratingquery, rest_rating_id=pk)
        currentRating.rating = request.data['rating']
        currentRating.desc = request.data['desc']
        currentRating.save()
        resp = JsonResponse({
            'message' : 'success',
        })
        return resp

class MenuRatingViewSet(viewsets.ModelViewSet):
    queryset = MenuRating.objects.all()
    serializer_class = MenuRatingSerializer
    def retrieve(self, request, pk=None, format=None):
        menu_id = self.request.query_params.get('menu_id')
        username = self.request.query_params.get('username')
        print(pk)
        print(self.request.query_params)
        try:
            menuquery = MenuInfo.objects.all()
            menu = get_object_or_404(menuquery, menu_id=menu_id)
            if username is '':
                menuRating = MenuRating.objects.filter(menu=menu)
            else :
                userquery = UserInfo.objects.all()
                user = get_object_or_404(userquery, username=username)
                menuRating = MenuRating.objects.filter(menu=menu, user=user)

        except:
            menuRating = MenuRating.objects.filter(menu_rating_id=pk)
            print("query error")

        serializer_context = {
            'request': request,
        }
        serializer = MenuRatingSerializer(menuRating, many=True, context=serializer_context)
        return Response(serializer.data)
    
    def create(self,request, pk=None, format=None):
        print(request.data)
        menuquery = MenuInfo.objects.all()
        menu = get_object_or_404(menuquery, menu_id=request.data['menu_id'])
        userinfoquery = UserInfo.objects.all()
        user = get_object_or_404(userinfoquery,username=request.data['username'])
        newMenuReview = MenuRating(menu= menu, user=user, rating=request.data['rating'], desc=request.data['desc'], like=0)
        newMenuReview.save()
        resp = JsonResponse({
            'message' : 'success',
        })
        return resp
    def update(self,request, pk=None, format=None):
        print(pk)
        print(request.data)
        menuratingquery = MenuRating.objects.all()
        currentRating = get_object_or_404(menuratingquery, menu_rating_id=pk)
        currentRating.rating = request.data['rating']
        currentRating.desc = request.data['desc']
        currentRating.save()
        resp = JsonResponse({
            'message' : 'success',
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp

class CommentListViewSet(viewsets.ModelViewSet):
    queryset = CommentList.objects.all()
    serializer_class = CommentListSerializer

# ref http://raccoonyy.github.io/drf3-tutorial-2/

# 유저 생성, 레스토랑 등록/삭제도 나중에 다 예외처리 해줘야함....


#insert order
@csrf_exempt
@api_view(['POST'])
def createOrder(request):
    # 추가, 삭제, 수정 버튼 필요
    if request.method == 'POST':
        print(request.data)
        #username, restarant_name, table_id, basket_menus(menu_id, count)
        restaurantquery = RestaurantInfo.objects.all()
        restaurant = get_object_or_404(restaurantquery, restaurant_name=request.data['restaurant_name'])
        userinfoquery = UserInfo.objects.all()
        try:
            user = get_object_or_404(userinfoquery, username = request.data['username'])
        except:
            user = get_object_or_404(userinfoquery, username = 'tempuser')
        
        try:
            orderquery = UserOrder.objects.all()
            current_order = get_object_or_404(orderquery, user=user,restaurant=restaurant, is_active=True)
        except:
            current_order = UserOrder(user=user,restaurant=restaurant, order_time=timezone.now(),
                                    table_id=request.data['table_id'], is_active=True)
            current_order.save()
        
        menuquery = MenuInfo.objects.all()
        for basket_menu in request.data['basket_menus']:
            selected_menu = get_object_or_404(menuquery, menu_id = basket_menu['menu_id'])
            new_order_contents = OrderContents(userorder = current_order, menu = selected_menu, menu_num = basket_menu['count'])
            new_order_contents.save()

        resp = JsonResponse({
            'message' : 'success',
            'order_id' : current_order.user_order_id
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp

#insert order
@csrf_exempt
@api_view(['POST'])
def changeOrder(request):
    # 추가, 삭제, 수정 버튼 필요
    if request.method == 'POST':
        print(request.data)
        userquery = UserInfo.objects.all()
        user = get_object_or_404(userquery, username= request.data['username'])
        tempuser = get_object_or_404(userquery, username='tempuser')
        userorderquery = UserOrder.objects.all()
        userorder = get_object_or_404(userorderquery, user_order_id=request.data['order_id'])
        
        print(userorder.user)
        print(user)
        userorder.user = user
        
        userorder.save()
        message='success'


        resp = JsonResponse({
            'message' : message,
        })
        return resp

@csrf_exempt
@api_view(['POST'])
def getActiveOrder(request):     # 메뉴 정보에서 편집 기능
    # 추가, 삭제, 수정 버튼 필요
    if request.method == 'POST':    # 메뉴 추가
        print(request.data)
        message='success'
        menus = []
        image=''
        order_id=0
        serializer_context = {
            'request': request,
        }
        try:
            restaurantquery = RestaurantInfo.objects.all()
            restaurant = get_object_or_404(restaurantquery, restaurant_name = request.data['restaurant_name'])
            serializer = RestaurantInfoSerializer(restaurant, context=serializer_context)
            image = serializer.data['restaurant_image']
            userorderquery = UserOrder.objects.all()
            userorder = get_object_or_404(userorderquery, restaurant = restaurant, table_id = request.data['table_id'], is_active=True)
            orderContents = OrderContents.objects.filter(userorder=userorder)
            order_id=userorder.user_order_id
        except:
            message = 'failed'
            orderContents = OrderContents.objects.all()


        serializer = OrderContentsSerializer(orderContents, many=True, context=serializer_context)

        resp = JsonResponse({
            'message' : message,
            'menus' : serializer.data,
            'restaurant_image' : image,
            'order_id' : order_id,
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp


@csrf_exempt
@api_view(['POST'])
def postImage(request):     # 메뉴 정보에서 편집 기능
    # 추가, 삭제, 수정 버튼 필요
    if request.method == 'POST':    # 메뉴 추가
        print("image uploaded")
        image = request.FILES.getlist('image')[0]
        if image is not None:
            new_image = ImageTable(image=image)
            new_image.save()
            serializer_context = {
                'request': request,
            }
            serializer = ImageTableSerializer(new_image,context=serializer_context)
            return Response(serializer.data)  
        else :
            resp = JsonResponse({
                'message' : 'fail',
            })
            resp['Access-Control-Allow-Origin'] = '*'
            print("before return")
            return resp  

#dosignup -> addSignup
@csrf_exempt
@api_view(['POST'])
def addSignup(request):
    if request.method == 'POST':
        #get data
        data=request.data
        print(data)

        print("----------------------")
        #TODO
        new_user = UserInfo(username=data['username'], first_name=data['first_name'], email=data['email'], is_owner=data['is_owner'])
        new_user.set_password(data['password'])
        new_user.is_active = False
        new_user.last_name = randstr(50)
        new_user.is_superuser = False
        new_user.is_staff = False
        new_user.date_joined = timezone.now()

        print("회원가입을 합니다.")

        mail = EmailMessage('BobView 사용자 인증', '안녕하세요 BobView입니다.\n사용자 인증은 위해서 아래 링크에 접속하시기 바랍니다.\n감사합니다.\n\n'
                            + 'http://127.0.0.1:8000/api/active/' + new_user.last_name, to=[data['email']])

        mail = EmailMessage('BobView 사용자 인증', '안녕하세요 BobView입니다.<br/>사용자 인증은 위해서 아래 링크에 접속하시기 바랍니다.<br/>감사합니다.<br/>'
        + "<a href=\"+http://127.0.0.1:8000/api/active/" + new_user.last_name+"\">링크</a>" + "<br/>http://127.0.0.1:8000/api/active/"+ new_user.last_name, to=[data['email']] )                            
        mail.content_subtype = "html"
        mail.send()

        print("before save")
        new_user.save()

        #set response. (dont need it here)
        resp = JsonResponse({
            'message' : 'success'
        })
        resp['Access-Control-Allow-Origin'] = '*'
        return resp

#dologin -> applyLogin
@csrf_exempt
@api_view(['POST'])
def applyLogin(request):
    if request.method == 'POST':
        #get data
        data=request.data
        print(data)

        print("----------------------")
        #TODO
        user = authenticate(username=data['username'], password=data['password'])
        message =''
        userinfo = None
        token = ''
        try:
            userinfo = get_object_or_404(UserInfo, username=data['username']) # 정보를 가져올 유저 객체를 가져온다
            token = userinfo.last_name
        except:
            result = 'fail'
            message = '로그인 실패. 다시 시도 해보세요.'
        
        

        if user is not None:
            login(request, user)
            result = 'success'
            message = '로그인 성공'
            is_owner = userinfo.is_owner
        else:
            result = 'fail'
            message = '로그인 실패. 다시 시도 해보세요.'
            is_owner = False

        #set response. (dont need it here)
        resp = JsonResponse({
            'result' : result,
            'message' : message,
            'token' : token,
            'is_owner': is_owner
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp

#check login data
@csrf_exempt
@api_view(['POST'])
def verifyLogin(request):
    if request.method == 'POST':
        #get data
        data=request.data
        print(data)

        print("----------------------")
        #TODO
        userinfo= None
        try:
            userinfo = get_object_or_404(UserInfo, username=data['username'], last_name=data['token']) # 정보를 가져올 유저 객체를 가져온다
        except:
            print('error')

        if userinfo is not None:
            result = True
        else:
            result = False

        #set response. (dont need it here)
        resp = JsonResponse({
            'result' : result,
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp

#modify user info data
@csrf_exempt
@api_view(['POST'])
def modifySignup(request):
    if request.method == 'POST':
        #get data
        data=request.data
        print(data)

        print("----------------------")
        #TODO
        userinfo= None
        message =''
        try:
            userinfo = get_object_or_404(UserInfo, username=data['username'], last_name=data['token']) # 정보를 가져올 유저 객체를 가져온다
        except:
            print('error')

        if userinfo.check_password(data['password']):
            print("check password success")
            userinfo.first_name = data['first_name']
            message = 'success'
        else:
            message = 'password error'

        if userinfo is not None:
            result = True
        else:
            result = False

        #set response. (dont need it here)
        resp = JsonResponse({
            'result' : message,
            'access' : result,
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp

#logout
@csrf_exempt
@api_view(['POST'])
def logout(request):
    if request.method == 'POST':
        #get data
        data=request.data
        print(data)

        print("----------------------")
        #TODO
        userinfo= None
        message =''
        try:
            userinfo = get_object_or_404(UserInfo, username=data['username'], last_name=data['token']) # 정보를 가져올 유저 객체를 가져온다
        except:
            print('error')

        if userinfo is not None:
            result = True
            userinfo.logout()
        else:
            result = False

        #set response. (dont need it here)
        resp = JsonResponse({
            'result' : message,
            'access' : result,
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp

'''
Things after this need refactoring
'''

def randstr(length):
    rstr = "0123456789abcdefghijklnmopqrstuvwxyzABCDEFGHIJKLNMOPQRSTUVWXYZ"
    rstr_len = len(rstr) - 1
    result = ""
    for i in range(length):
        result += rstr[random.randint(0, rstr_len)]
    return result

def user_active(request, token):
    user = get_object_or_404(UserInfo, last_name=token)
    if user.date_joined < timezone.now() - datetime.timedelta(days=7): # 일주일 지나면 만료
        user.delete()
        message = "만료된 링크입니다. 다시 가입을 신청하세요."
    else:
        user.is_active = True
        user.save()
        message = "이메일이 인증되었습니다."
    return render(request, 'myapp/success.html', {'message':message })