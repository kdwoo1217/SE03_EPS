from typing import List, Any

from django.shortcuts import render

from django.db import connection
from django.http import HttpResponse
from django.shortcuts import render
from django.views.generic import View
from django.contrib.auth import authenticate
from django.http import HttpResponseRedirect, HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.db import connection
from django.db.models import Q
from django.db.models import Prefetch
from parkinglot.models import *
from datetime import datetime
from parkinglot.models import id
from parkinglot.models import parkinglotInfo
import json
from random import *

is_test = False
# Create your views here.
# def getCarCount(request):
#   print("hello")

def signIn(request):
    data = {}
    data['id'] = request.GET.get("id")
    data['password'] = request.GET.get("password")
    getNote = {}
    getNote2 = ""
    temp = {}

    if id.objects.filter(userID=data['id'], password=data['password']).exists():
        getNote = id.objects.only('parkinglotID').get(userID=data['id'], password=data['password']).parkinglotID.parkinglotID
        getNote2 = id.objects.only('parkinglotID').get(userID=data['id'], password=data['password']).parkinglotID.note
        temp['parkinglotID'] = getNote
        temp['note'] = getNote2

        return HttpResponse(json.dumps(temp))
    else:
        return HttpResponse(0)

def signUp(request):
    data = {}
    data['id'] = request.GET.get("id")
    data['password'] = request.GET.get("password")
    data['name'] = request.GET.get("name")
    data['x'] = float(request.GET.get("x"))
    data['y'] = float(request.GET.get("y"))
    data['locationCode'] = float(request.GET.get("locationCode"))
    data['totalSpace'] = int(request.GET.get("totalSpace"))
    data['isFree'] = int(request.GET.get("isFree"))              # 0이면 공짜 1이면 유료
    data['note'] = request.GET.get("note")
    data['parkinglotID'] = randrange(10000)

    parkinglotInfo.objects.create(parkinglotID=data['parkinglotID'], name=data['name'], x=data['x'], y=data['y'], locationCode=data['locationCode'],
                                         totalSpace=data['totalSpace'], currentSpace=data['totalSpace'],
                                         isFree=data['isFree'], note=data['note'])

    iqi = id.objects.create(parkinglotID_id=data['parkinglotID'], userID=data['id'], password=data['password'])

    return HttpResponse("OK")

def updateNote(request):
    data = {}
    data['parkinglotID'] = request.GET.get('parkinglotID')
    data['note'] = request.GET.get('note')
    if parkinglotInfo.objects.filter(parkinglotID=data['parkinglotID']).exists():
        parkinglotInfo.objects.filter(parkinglotID=data['parkinglotID']).update(currentSpace=data['note'])
        return HttpResponse("Update Complete : " + data['parkinglotID'])
    else:
        return HttpResponse("Non-Exist Parking Lot : " + data['parkinglotID'])

def isExistID(request):
    data={}
    data['id'] = request.GET.get("id")
    if id.objects.filter(userID=data['id']).exists():
        return HttpResponse(1)
    else:
        return HttpResponse(0)

def updateCarCount(request):
    data={}
    data['parkinglotID'] = request.GET.get('parkinglotID')
    data['carCount'] = request.GET.get('carCount')
    if parkinglotInfo.objects.filter(parkinglotID=data['parkinglotID']).exists():
        parkinglotInfo.objects.filter(parkinglotID=data['parkinglotID']).update(currentSpace=data['carCount'])
        return HttpResponse("Update Complete : " + data['parkinglotID'])
    else:
        return HttpResponse("Non-Exist Parking Lot : " + data['parkinglotID'])

def getParkingInfo(request):
    data = {}
    data['location'] = request.GET.get('location')

    data2 = list()
    text = list()
    data2 = parkinglotInfo.objects.filter(locationCode=int(data['location']))

    text += data2.values('name', 'x', 'y', 'totalSpace', 'currentSpace', 'isFree', 'note')

    return HttpResponse(json.dumps(text))