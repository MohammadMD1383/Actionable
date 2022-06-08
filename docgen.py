import glob
import os.path
import re
from re import compile
from textwrap import dedent


class Documentation:
	def __init__(self, name: str, title: str, description: str, example: str):
		self.name = name
		self.title = title
		self.description = description
		self.example = example
	
	def __str__(self):
		return '\n\n'.join([
			f'## {self.title}',
			f'{self.description}',
			'---',
			'### example:',
			f'{self.example}'
		])


doc_pattern = compile(r'@Documentation\(.*title = "(.*?)".*description = "(.*?)".*example = """(.*?)""".*?\n\)', re.DOTALL | re.MULTILINE)
name_pattern = compile(r'([a-zA-Z]+)\..+$')
documentations: list[Documentation] = []


def all_files():
	for path in glob.iglob("src/main/java/ir/mmd/intellijDev/Actionable/**", recursive=True):
		if os.path.isfile(path):
			yield path


def process_annotation(path: str, content: str):
	name_match = name_pattern.search(path)
	doc_match = doc_pattern.search(content)
	if doc_match is not None:
		documentations.append(Documentation(
			name_match.groups()[0],
			doc_match.groups()[0],
			doc_match.groups()[1],
			dedent(doc_match.groups()[2]).strip()
		))


if __name__ == '__main__':
	for file_path in all_files():
		with open(file_path, 'r') as file:
			process_annotation(file_path, file.read())
	
	if not os.path.exists("docs"):
		os.mkdir("docs")
	else:
		for file in glob.glob("docs/*"):
			os.remove(file)
	
	with open('docs/index.md', 'w') as index:
		for doc in documentations:
			index.write(f"[{doc.title}]({doc.name}.md)\n")
			
			with open(f'docs/{doc.name}.md', 'w') as file:
				file.write(doc.__str__())
