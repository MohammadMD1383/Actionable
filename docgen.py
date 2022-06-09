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


doc_pattern = compile(
	r'@Documentation\(.*?title = "(.*?)".*?description = "(.*?)".*?example = """(.*?)""".*?\n\).*?class ([a-zA-Z]+)',
	re.DOTALL | re.MULTILINE
)
documentations: list[Documentation] = []


def all_files():
	for path in glob.iglob("src/main/java/ir/mmd/intellijDev/Actionable/**", recursive=True):
		if os.path.isfile(path):
			yield path


def process_annotation(content: str):
	for match in doc_pattern.finditer(content):
		documentations.append(Documentation(
			match.groups()[3],
			match.groups()[0],
			match.groups()[1],
			dedent(match.groups()[2]).strip()
		))


if __name__ == '__main__':
	for file_path in all_files():
		with open(file_path, 'r') as file:
			process_annotation(file.read())
	
	if not os.path.exists("docs"):
		os.mkdir("docs")
	else:
		for file in glob.glob("docs/*"):
			os.remove(file)
	
	with open('docs/_config.yml', 'w') as config:
		config.write('theme: jekyll-theme-cayman')
	
	with open('docs/index.md', 'w') as index:
		index.write('\n'.join([
			'---',
			'title: Actionable',
			'---',
			'',
			'<iframe frameborder="none" width="384px" height="325px" src="https://plugins.jetbrains.com/embeddable/card/17962"></iframe>',
			'',
			'## Features:'
		]) + '\n\n')
		
		for doc in documentations:
			file_name = f'{doc.name}.md'
			index.write(f"[{doc.title}]({file_name})\n\n")
			
			with open(f'docs/{file_name}', 'w') as file:
				file.write('\n'.join([
					'---',
					'title: Actionable',
					'---'
				]) + '\n\n')
				file.write(doc.__str__())
				file.write('\n\n[&larr; Back](index.md)')
