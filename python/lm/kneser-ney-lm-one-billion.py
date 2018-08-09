from pathlib import Path
import pickle
from ngram import KneserNeyNGram


pathlist = Path("resources/word-freq-dumps/1-billion-word-language-" + \
          "modeling-benchmark-r13output/training-monolingual.tokenized.shuffled/").glob('**/*')
max_files = 5
sentences = []
characters = []
for index, path in enumerate(pathlist):
    # because path is object not string
    path_in_str = str(path)
    print('Reading : {}'.format(path_in_str))
    with open(path_in_str, encoding="utf8") as file:
        sentences.extend([line.strip().split() for line in file])
        characters.extend([[character for word in sentence for character in word]
                      for sentence in sentences])
    if index >= max_files-1:
        break
print('Len sentences : {}'.format(len(sentences)))
print('Len characters : {}'.format(len(characters)))


ngram_char_3 = KneserNeyNGram(n=3, sents=characters, D = 1)

with open('resources/language-models/ngram_char_3.json', 'wb') as fp:
        pickle.dump(ngram_char_3, fp, protocol=pickle.HIGHEST_PROTOCOL)