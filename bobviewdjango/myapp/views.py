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

#serializer
from rest_framework import viewsets
from myapp.serializers import UserInfoSerializer, RestaurantInfoSerializer, OrderSerializer, MenuInfoSerializer, OrderMenuSerializer
from myapp.models import UserInfo, RestaurantInfo, Order, MenuInfo, OrderMenu
# Create your views here.
#api
class UserInfoViewSet(viewsets.ModelViewSet):
    queryset = UserInfo.objects.all()
    serializer_class = UserInfoSerializer

class RestaurantInfoViewSet(viewsets.ModelViewSet):
    queryset = RestaurantInfo.objects.all()
    serializer_class = RestaurantInfoSerializer

class OrderViewSet(viewsets.ModelViewSet):
    queryset = Order.objects.all()
    serializer_class = OrderSerializer

class MenuInfoViewSet(viewsets.ModelViewSet):
    queryset = MenuInfo.objects.all()
    serializer_class = MenuInfoSerializer

class OrderMenuViewSet(viewsets.ModelViewSet):
    queryset = OrderMenu.objects.all()
    serializer_class = OrderMenuSerializer

# trans

from django.views.decorators.csrf import csrf_exempt
from django.http import HttpResponse, JsonResponse
from myapp.models import Post
from rest_framework.decorators import api_view
from rest_framework.response import Response

#template
@csrf_exempt
@api_view(['POST'])
def testing(request):
    if request.method == 'POST':
        #get data
        data=request.data
        print(data)

        print("----------------------")
        #TODO

        #set response
        resp = JsonResponse({
            'message' : 'hello vue'
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp

#ref http://raccoonyy.github.io/drf3-tutorial-2/

# 유저 생성, 레스토랑 등록/삭제도 나중에 다 예외처리 해줘야함....


@csrf_exempt
@api_view(['GET', 'POST', 'DELETE'])
def myrestaurant(request):    
    data=request.data
    # 레스토랑페이지에서 기본적인 레스토랑 정보 + 메뉴 정보를 보여주기
    if request.method == 'GET':
        user = get_object_or_404(UserInfo, username=data['username'])      # 유저 객체를 가져온다     
        try:
            restaurant = RestaurantInfo.object.get(owner=user)             # 레스토랑 객체를 가져온다
            restaurant_name = restaurant.name
            restaurant_address = restaurant.restaurant_address
            restaurant_image = restaurant_image
            menus = []
            for MenuInfo in MenuInfo.objects.filter(RestaurantInfo=restaurant):
                MenuInfos.append(MenuInfo)
            resp = JsonResponse({
            'message' : 'success',
            'itmes'   : [restaurant, menus]
            })
        except Exception:       
            resp = JsonResponse({
            'message' : 'no_res_info'
            })
        
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp
    # 레스토랑을 등록 했을때!
    if request.method == 'POST': 

        '''
        restaurant_name = models.CharField(primary_key=True, max_length=45, unique=True)
        restaurant_address = models.CharField(max_length=128, unique=True)
        restaurant_latitude = models.IntegerField()
        restaurant_longitude = models.IntegerField()
        restaurant_image = models.CharField(max_length=1024, unique=True)
        '''

        new_restaurant = RestaurantInfo(restaurant_name=data['restaurant_name'], restaurant_address=data['restaurant_address'], 
                                        restaurant_latitude=data['restaurant_latitude'], restaurant_longitude=data['restaurant_longitude'],
                                        restaurant_image=data['restaurant_image']) # 이미지는 로컬에 저장하는거 해야됨.....
        new_restaurant.save()

        resp = JsonResponse({
            'message' : 'success',
            'itmes'   : [new_restaurant]
            })
        
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp
    # 레스토랑을 삭제 했을때!
    if request.method == 'DELETE': 
        
        RestaurantInfo.objects.get(restaurant_name=data['restaurant_name']).delete()

        resp = JsonResponse({
        'message' : 'success'
        })
        
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp

@csrf_exempt
@api_view(['GET'])
def mypage(request):    
    # 기본적인 마이페이지에 유저의 정보를 표시하기
    if request.method == 'GET':
        user = get_object_or_404(UserInfo, username=data['username']) # 정보를 가져올 유저 객체를 가져온다

        #TODO
        username = user.username
        join_data = user.date_joined
        last_login = user.last_login
        name = user.first_name
        email = user.email
        is_owner = user.is_owner

        #set response.
        resp = JsonResponse({
            'message' : 'success',
            'itmes'   : [username, join_data, last_login, name, email, is_owner]
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp

    # 마이페이지에서 무언가를 수정했을때
    elif request.method == 'PUT':
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

        username = user.username
        join_data = user.date_joined
        last_login = user.last_login
        name = user.first_name
        email = user.email
        is_owner = user.is_owner

        #set response.
        resp = JsonResponse({
            'message' : 'success',
            'itmes'   : [username, join_data, last_login, name, email, is_owner]
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp

#dosignup -> signup
@csrf_exempt
@api_view(['POST'])
def signup(request):
    if request.method == 'POST':
        #get data
        data=request.data
        print(data)

        print("----------------------")
        #TODO
        new_user = UserInfo(username=data['username'], first_name=data['first_name'], email=data['email'], is_owner=data['is_owner'])
        new_user.set_password(data['password'])
        new_user.is_active = False
        new_user.last_name = randstr(5)
        new_user.is_superuser = False
        new_user.is_staff = False
        new_user.date_joined = timezone.now()

        print("회원가입을 합니다.")

        mail = EmailMessage('BobView 사용자 인증', '안녕하세요 BobView입니다.\n사용자 인증은 위해서 아래 링크에 접속하시기 바랍니다.\n감사합니다.\n\n' 
                                                    + 'http://127.0.0.1:8000/api/active/' + new_user.last_name, to=[data['email']])
        mail.content_subtype = "html"
        mail.send()

        print("before save")
        new_user.save()

        message = data['email'] + "로 메일을 전송하였습니다. 메일을 확인해주세요"

        #set response. (dont need it here)
        resp = JsonResponse({
            'message' : message
        })
        resp['Access-Control-Allow-Origin'] = '*'
        print("before return")
        return resp

'''
Things after this need refactoring
'''

def dologin(request):
    if request.method == 'POST':
        username = request.POST['username']
        password = request.POST['password']
        print(username)
        print(password)
        user = authenticate(username=username, password=password)
        print(user)
        if user is not None:
            login(request, user)
            return redirect('success')
        else:
            return HttpResponse('로그인 실패. 다시 시도 해보세요.')


def dosignup(request):
    if request.method == 'POST':

        # new_user = User.objects.create_user(username=username, first_name=first_name, last_name=last_name, email=email, password=password, is_owner=is_owner)
        new_user = UserInfo(username=userdata.username, first_name=userdata.first_name, email=userdata.email, is_owner=userdata.is_owner)
        new_user.set_password(userdata.password)
        new_user.is_active = False
        new_user.last_name = randstr(50)
            
        print("회원가입을 합니다.")

        mail = EmailMessage('BobView 사용자 인증', '안녕하세요 BobView입니다.\n사용자 인증은 위해서 아래 링크에 접속하시기 바랍니다.\n감사합니다.\n\n' 
                                                    + 'http://127.0.0.1:8000/active/' + new_user.last_name, to=[userdata.email])
        mail.content_subtype = "html"
        mail.send()

        message = new_user.first_name + "님께서 입력하신 메일로 인증 링크를 발송했습니다."

        new_user.save()

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
        user.last_name = ''
        user.save()
        message = "이메일이 인증되었습니다."
    return render(request, 'myapp/success.html', {'message':message })


# class UserRegistrationView(CreateView):
#     model = get_user_model()
#     form_class = UserRegistrationForm
#     success_url = '/user/login/'
#     verify_url = '/user/verify/'
#     token_generator = default_token_generator

#     def form_valid(self, form):
#         response = super().form_valid(form)
#         if form.instance:
#             self.send_verification_email(form.instance)
#         return response

#     def send_verification_email(self, user):
#         token = self.token_generator.make_token(user)
#         user.email_user('회원가입을 축하드립니다.', '다음 주소로 이동하셔서 인증하세요. {}'.format(self.build_verification_link(user, token)), from_email=settings.EMAIL_HOST_USER)
#         messages.info(self.request, '회원가입을 축하드립니다. 가입하신 이메일주소로 인증메일을 발송했으니 확인 후 인증해주세요.')

#     def build_verification_link(self, user, token):
#         return '{}/user/{}/verify/{}/'.format(self.request.META.get('HTTP_ORIGIN'), user.pk, token)

# class UserVerificationView(TemplateView):

#     model = get_user_model()
#     redirect_url = '/user/login/'
#     token_generator = default_token_generator

#     def get(self, request, *args, **kwargs):
#         if self.is_valid_token(**kwargs):
#             messages.info(request, '인증이 완료되었습니다.')
#         else:
#             messages.error(request, '인증이 실패되었습니다.')
#         return HttpResponseRedirect(self.redirect_url)   # 인증 성공여부와 상관없이 무조건 로그인 페이지로 이동

#     def is_valid_token(self, **kwargs):
#         pk = kwargs.get('pk')
#         token = kwargs.get('tonen')
#         user = self.model.objects.get(pk=pk)
#         is_valid = self.token_generator.check_token(user, token)
#         if is_valid:
#             user.is_active = True
#             user.save()     # 데이터가 변경되면 반드시 save() 메소드 호출
#         return is_valid
