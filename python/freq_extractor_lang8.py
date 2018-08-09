from collections import defaultdict
import re

word_uses = defaultdict(int)
save = []
with open("resources/word-freq-dumps/lang-8-en-1.0/entries.train", encoding="utf8") as file:
    for line in file:
        values = line.split('\t')
        values = [word.strip().lower() for sentence in values[4:(len(values))]
                  for word in re.sub(r'[^\w\s]','',sentence).split()]
        for word in values:
            word_uses[word] += 1
            

encoding_errors = 0
words = list(word_uses.keys())
words.sort(key=lambda w: word_uses[w], reverse=True)
with open('resources/word-freq-dumps/word_freqs_lang8.txt', 'w') as file:
   for word in words:
       print("%s %d" % (word, word_uses[word]))
       try:
           file.write('{} {}\n'.format(word, str(word_uses[word])))
       except UnicodeEncodeError:
           encoding_errors+=1


print('Word extracted : {}'.format(len(word_uses)))
print('Corpus size : {}'.format(sum(word_uses.values())))