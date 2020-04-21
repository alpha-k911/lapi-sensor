import requests
import time
import os

def setBrightness():
	factor = 750
	r = requests.get('https://stately-list-230614.firebaseio.com/lux.json')
	level = int(r.content.decode())
	print("Lux: ",level)
	
	if level in range(0,30):
		brightness = factor * 2
	elif level in range(30,100):
		brightness = factor * 3
	elif level in range(100,500):
		brightness = factor * 4
	elif level in range(500,1000):
		brightness = factor * 5
	elif level in range(1000,2000):
		brightness = factor * 6
	else:
		brightness = factor * 8
	
	print("Brightness set to: ",end="\n")
	
	os.system("echo " + str(brightness) + " | sudo tee /sys/class/backlight/intel_backlight/brightness")
	
	time.sleep(1*5)
	
if __name__ == '__main__':
	while True:
		setBrightness()
		time.sleep(1*5)
		
	
