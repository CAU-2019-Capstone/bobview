# Generated by Django 2.2.6 on 2019-10-12 11:54

from django.conf import settings
import django.contrib.auth.validators
from django.db import migrations, models
import django.db.models.deletion
import django.utils.timezone


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('auth', '0011_update_proxy_permissions'),
    ]

    operations = [
        migrations.CreateModel(
            name='UserInfo',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('password', models.CharField(max_length=128, verbose_name='password')),
                ('last_login', models.DateTimeField(blank=True, null=True, verbose_name='last login')),
                ('is_superuser', models.BooleanField(default=False, help_text='Designates that this user has all permissions without explicitly assigning them.', verbose_name='superuser status')),
                ('username', models.CharField(error_messages={'unique': 'A user with that username already exists.'}, help_text='Required. 150 characters or fewer. Letters, digits and @/./+/-/_ only.', max_length=150, unique=True, validators=[django.contrib.auth.validators.UnicodeUsernameValidator()], verbose_name='username')),
                ('first_name', models.CharField(blank=True, max_length=30, verbose_name='first name')),
                ('last_name', models.CharField(blank=True, max_length=150, verbose_name='last name')),
                ('email', models.EmailField(blank=True, max_length=254, verbose_name='email address')),
                ('is_staff', models.BooleanField(default=False, help_text='Designates whether the user can log into this admin site.', verbose_name='staff status')),
                ('is_active', models.BooleanField(default=True, help_text='Designates whether this user should be treated as active. Unselect this instead of deleting accounts.', verbose_name='active')),
                ('date_joined', models.DateTimeField(default=django.utils.timezone.now, verbose_name='date joined')),
                ('is_owner', models.BooleanField(default=False)),
                ('groups', models.ManyToManyField(blank=True, help_text='The groups this user belongs to. A user will get all permissions granted to each of their groups.', related_name='user_set', related_query_name='user', to='auth.Group', verbose_name='groups')),
                ('user_permissions', models.ManyToManyField(blank=True, help_text='Specific permissions for this user.', related_name='user_set', related_query_name='user', to='auth.Permission', verbose_name='user permissions')),
            ],
            options={
                'db_table': 'user_info',
                'managed': True,
            },
        ),
        migrations.CreateModel(
            name='MenuInfo',
            fields=[
                ('menu_id', models.CharField(max_length=45, primary_key=True, serialize=False)),
                ('menu_name', models.CharField(max_length=45)),
                ('menu_price', models.IntegerField()),
                ('menu_desc', models.TextField()),
                ('menu_rating', models.FloatField(blank=True, null=True)),
                ('menu_image', models.CharField(max_length=1024)),
            ],
            options={
                'db_table': 'menu_info',
                'managed': True,
            },
        ),
        migrations.CreateModel(
            name='Order',
            fields=[
                ('order_id', models.CharField(max_length=45, primary_key=True, serialize=False)),
                ('order_time', models.DateTimeField()),
                ('tot_price', models.IntegerField()),
                ('table_id', models.IntegerField()),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
            options={
                'db_table': 'order',
                'managed': True,
                'unique_together': {('user', 'order_id')},
            },
        ),
        migrations.CreateModel(
            name='Post',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=255)),
                ('content', models.TextField()),
            ],
        ),
        migrations.CreateModel(
            name='RestaurantInfo',
            fields=[
                ('restaurant_name', models.CharField(max_length=45, primary_key=True, serialize=False)),
                ('restaurant_gps', models.CharField(max_length=45)),
                ('restaurant_rating', models.FloatField(blank=True, null=True)),
                ('restaurant_image', models.CharField(max_length=1024)),
                ('owner', models.OneToOneField(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
            options={
                'db_table': 'restaurant_info',
                'managed': True,
            },
        ),
        migrations.CreateModel(
            name='OrderMenu',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('menu_tot_price', models.IntegerField()),
                ('menu_num', models.IntegerField()),
                ('menu', models.ManyToManyField(to='myapp.MenuInfo')),
                ('order', models.ManyToManyField(to='myapp.Order')),
            ],
            options={
                'db_table': 'order_menu',
                'managed': True,
            },
        ),
        migrations.AddField(
            model_name='menuinfo',
            name='restaurant',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='myapp.RestaurantInfo'),
        ),
    ]
