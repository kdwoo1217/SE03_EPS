# Generated by Django 2.0.6 on 2018-06-09 13:40

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('parkinglot', '0005_auto_20180609_2235'),
    ]

    operations = [
        migrations.AlterField(
            model_name='id',
            name='parkinglotID',
            field=models.ForeignKey(db_constraint=False, null=True, on_delete=django.db.models.deletion.CASCADE, to='parkinglot.parkinglotInfo'),
        ),
    ]
