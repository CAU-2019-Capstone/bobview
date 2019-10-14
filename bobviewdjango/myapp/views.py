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
        user = UserInfo.objects.filter(username=pk)
        serializer = UserInfoSerializer(user,many=True,context=serializer_context)
        return Response(serializer.data)
        

class RestaurantInfoViewSet(viewsets.ModelViewSet):
    queryset = RestaurantInfo.objects.all()
    serializer_class = RestaurantInfoSerializer

    def retrieve(self, request, pk=None):
        userqueryset = UserInfo.objects.all()
        user = get_object_or_404(userqueryset, username = pk)
        serializer_context = {
            'request': request,
        }
        restaurant = get_object_or_404(self.queryset, owner = user)
        serializer = RestaurantInfoSerializer(restaurant, context=serializer_context)
        return Response(serializer.data)
    
    def update(self, request, pk=None):
        userqueryset = UserInfo.objects.all()
        user = get_object_or_404(userqueryset, username = pk)
        serializer_context = {
            'request': request,
        }
        restaurant = get_object_or_404(self.queryset, owner = user)
        restaurant.restaurant_name = request.data['restaurant_name']
        restaurant.restaurant_address = request.data['restaurant_address']
        restaurant.restaurant_latitude = request.data['restaurant_latitude']
        restaurant.restaurant_longitude = request.data['restaurant_longitude']
        restaurant.save()

class MenuInfoViewSet(viewsets.ModelViewSet):
    queryset = MenuInfo.objects.all()
    serializer_class = MenuInfoSerializer

    def retrieve(self, request, pk=None):
        serializer_context = {
            'request': request,
        }
        menu_name = self.request.query_params.get('menu_name')
        restaurant = None
        if menu_name is not None :
            restaurant = MenuInfo.objects.filter(restaurant_id=pk, menu_name = menu_name)
        else :
            restaurant = MenuInfo.objects.filter(restaurant_id=pk)
        serializer = MenuInfoSerializer(restaurant,many=True,context=serializer_context)
        return Response(serializer.data)

    def get_queryset(self):
        queryset = self.queryset
        menu_name = self.request.query_params.get('menu_name')
        if menu_name is not None :
            queryset = MenuInfo.objects.filter(menu_name = menu_name)
        return queryset

class UserOrderViewSet(viewsets.ModelViewSet):
    queryset = UserOrder.objects.all()
    serializer_class = UserOrderSerializer

class OrderContentsViewSet(viewsets.ModelViewSet):
    queryset = OrderContents.objects.all()
    serializer_class = OrderContentsSerializer

class RestRatingViewSet(viewsets.ModelViewSet):
    queryset = RestRating.objects.all()
    serializer_class = RestRatingSerializer

class MenuRatingViewSet(viewsets.ModelViewSet):
    queryset = MenuRating.objects.all()
    serializer_class = MenuRatingSerializer

# ref http://raccoonyy.github.io/drf3-tutorial-2/

# 유저 생성, 레스토랑 등록/삭제도 나중에 다 예외처리 해줘야함....

@csrf_exempt
@api_view(['GET', 'DELETE'])
def mymenu_get_delete(request):     # 메뉴 정보에서 편집 기능
    # 해당 레스토랑의 메뉴 정보 보여주기
    if request.method == 'GET':
        try:
            menuqueryset = MenuInfo.objects.filter(restaurant_id=request.GET['restaurant_id'])
            
        except MenuInfo.DoesNotExist:
            return HttpResponse(status=404)
        serializer_context = {
            'request': request,
        }
        serializer = MenuInfoSerializer(menuqueryset,many=True, context=serializer_context)
        return Response(serializer.data)

    if request.method == 'DELETE':      # 메뉴 삭제
        try:
            menu = MenuInfo.objects.get(restaurant_id=request.DELETE['restaurant_id'], menu_id=request.DELETE['menu_id'])
        except MenuInfo.DoesNotExist:
            return HttpResponse(status=404)
        menu.delete()
        edited_menus = MenuInfo.objects.get(restaurant_id=request.GET['restaurant_id'])
        serializer = MenuInfoSerializer(edited_menus, many=True)
        return Response(serializer.data)

@csrf_exempt
@api_view(['POST', 'PUT'])
def mymenu_post_put(request):     # 메뉴 정보에서 편집 기능
    # 추가, 삭제, 수정 버튼 필요
    if request.method == 'POST':    # 메뉴 추가
        data=request.data
        restaurant_name = data['restaurant_name']
        restaurant = get_object_or_404(RestaurantInfo, restaurant_name=restaurant_name)
        new_menu = MenuInfo(restaurant=restaurant, menu_name=data['menu_name'], 
                            menu_price = data['menu_price'], menu_desc = data['menu_desc'], menu_image=data['menu_image'])
        new_menu.save()

        resp = JsonResponse({
            'message' : 'success',
        })
        
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp  

        # data = JSONParser().parse(request)
        # serializer = MenuInfoSerializer(data=data)

        # if serializer.is_valid():
        #     serializer.save()
        #     return JsonResponse(serializer.data, status=201)
        # else:
        #     return JsonResponse(serializer.errors, status=400) 
        
    if request.method == 'PUT':         # 메뉴 수정 
        data=request.data
        # 마이페이지 수정 참고
        renew = MenuInfo.objects.get(menu_name = data['menu_name'])  # 정보를 가져올 메뉴 객체를 가져온다
        
        renew.menu_name = data['new_menuname']
        renew.menu_price = data['new_menuprice']
        renew.menu_desc = data['new_menudesc']
        renew.menu_image = data['new_menuimage']
        renew.save()

        edited_menus = MenuInfo.objects.get(restaurant_name=request.GET['restaurant_name'])
        serializer = MenuInfoSerializer(edited_menus, many=True)
        return JsonResponse(serializer.data)

        # resp['Access-Control-Allow-Origin'] = '*'
        # print("before return")
        # return resp 

        # data = JSONParser().parse(request)
        # serializer = MenuSerializer(menu, data=data)
        # if serializer.is_valid():
        #     serializer.save()
        #     return JSONResponse(serializer.data)
        # else:
        #     return JSONResponse(serializer.errors, status=400)

@csrf_exempt
@api_view(['GET', 'DELETE'])
def myrestaurant_get_delete(request, username):    
    # 레스토랑페이지에서 기본적인 레스토랑 정보 + 메뉴 정보를 보여주기
    if request.method == 'GET':
        username = request.GET["username"]
        user = get_object_or_404(UserInfo, username=username)      # 유저 객체를 가져온다     

        restaurant = RestaurantInfo.object.get(owner=user)             # 레스토랑 객체를 가져온다

        serializer = MenuInfoSerializer(restaurant)
        return JsonResponse(serializer.data)

    # 레스토랑을 삭제 했을때!
    if request.method == 'DELETE': 
        restaurant_name = request.GET["restaurant_name"]
        RestaurantInfo.objects.get(restaurant_name=restaurant_name).delete()

        resp = JsonResponse({
            'message' : 'success'
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp

@csrf_exempt
@api_view(['POST'])
def myrestaurant_post(request):    
    # 레스토랑을 등록 했을때!
    if request.method == 'POST': 
        data=request.data
        owner = get_object_or_404(UserInfo, username=data['username'])

        new_restaurant = RestaurantInfo(restaurant_name=data['restaurant_name'], restaurant_address=data['restaurant_address'], 
                                        restaurant_latitude=data['restaurant_latitude'], restaurant_longitude=data['restaurant_longitude'],
                                        restaurant_image=data['restaurant_image'], owner=owner) # 이미지는 로컬에 저장하는거 해야됨.....
        new_restaurant.save()
        serializer = RestaurantInfoSerializer(new_restaurant)
        return JsonResponse(serializer.data)

@csrf_exempt
@api_view(['GET'])
def mypage_get(request):    
    # 기본적인 마이페이지에 유저의 정보를 표시하기
    if request.method == 'GET':
        username = request.GET['username']
        user = get_object_or_404(UserInfo, username=username) # 정보를 가져올 유저 객체를 가져온다
        serializer = UserInfoSerializer(user)
        return JsonResponse(serializer.data)

@csrf_exempt
@api_view(['PUT'])
def mypage_put(request):    
    # 마이페이지에서 무언가를 수정했을때
    if request.method == 'PUT':
        data=request.data
        user = get_object_or_404(UserInfo, username=data['username']) # 정보를 가져올 유저 객체를 가져온다
        if 'new_username' in data.keys():
            try :
                user.username = data['new_username']
            except Exception:  
                username_reply = '이미 사용중인 아이디입니다.'
        if 'new_email' in data.keys():
            try :
                user.email = data['new_email']
            except Exception:  
                email_reply = '이미 사용중인 이메일입니다.'
        if 'new_name' in data.keys():
            user.first_name = data['new_name']
        if 'new_is_owner' in data.keys():
            user.is_owner = data['new_is_owner']
        user.save()

        serializer = UserInfoSerializer(user)
        return JsonResponse(serializer.data)

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