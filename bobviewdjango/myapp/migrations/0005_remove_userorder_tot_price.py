# Generated by Django 2.2.6 on 2019-11-04 12:14

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('myapp', '0004_userorder_is_active'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='userorder',
            name='tot_price',
        ),
    ]
