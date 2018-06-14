from django.db import models

# Create your models here.

class parkinglotInfo(models.Model):
    parkinglotID = models.IntegerField(primary_key=True)

    name = models.CharField(max_length=45)          # 주차장 이름
    x = models.FloatField()          # 위도
    y = models.FloatField()          # 경도
    locationCode = models.IntegerField()   # 지역 코드

    totalSpace = models.IntegerField()     # 총 주차장 칸 개수
    currentSpace = models.IntegerField()   # 남은 주차장 칸 개수

    isFree = models.IntegerField()         # 유/무료 여부
    note = models.CharField(max_length=200)              # 메모

class id(models.Model):

    parkinglotID = models.ForeignKey(parkinglotInfo, on_delete=models.CASCADE, null=True, db_constraint=False)
    userID = models.CharField(max_length=20, primary_key=True)  # 유저 아이디
    password = models.CharField(max_length=20)                  # 비밀번호

