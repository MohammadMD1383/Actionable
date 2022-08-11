from kopyt import Parser, node

from util import all_files

all_classes: list[node.ClassDeclaration] = []

if __name__ == '__main__':
	for f in [f for f in all_files() if f.endswith(".kt")]:
		with open(f, 'r') as file:
			ast = Parser(file.read()).parse()
			for declaration in [d for d in ast.declarations if isinstance(d, node.ClassDeclaration)]:
				all_classes.append(declaration)
