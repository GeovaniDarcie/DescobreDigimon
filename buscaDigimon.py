import pyautogui as py
import time
import sys

digimon = sys.argv[1]


py.press('win')
py.write('chrome')
py.press('enter')
time.sleep(1)
py.write(f'https://wikimon.net/File:{digimon}.jpg')
py.press('enter')
py.moveTo(344, 534)
time.sleep(1)
py.click()
