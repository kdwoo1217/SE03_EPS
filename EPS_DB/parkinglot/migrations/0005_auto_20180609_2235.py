# Generated by Django 2.0.6 on 2018-06-09 13:35

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('parkinglot', '0004_id_pid'),
    ]

    operations = [
        migrations.RenameField(
            model_name='id',
            old_name='pID',
            new_name='parkinglotID',
        ),
    ]