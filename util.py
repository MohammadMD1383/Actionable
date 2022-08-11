import glob
import os


def all_files():
	for path in glob.iglob("src/main/java/ir/mmd/intellijDev/Actionable/**", recursive=True):
		if os.path.isfile(path):
			yield path
