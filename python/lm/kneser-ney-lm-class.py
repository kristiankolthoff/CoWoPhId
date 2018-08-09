import pickle
from ngram import KneserNeyNGram
import pandas as pd
from collections import namedtuple

Dataset = namedtuple('Dataset', 'name, train, test')
MAIN_PATH_DATASET = "../../cwishareddataset/traindevset/english/"
genres = ['Wikipedia', 'WikiNews', 'News']
datasets = ['Train', 'Dev']
columns = ['id', 'sentence', "start", "end", "target", 
           "nat", "non_nat", "nat_marked", "non_nat_marked", "binary", "prob"]


datasets = [Dataset('Wikipedia', 'Train', 'Dev'),
            Dataset('WikiNews', 'Train', 'Dev'),
            Dataset('News', 'Train', 'Dev')]

feature_categories = []

def load_df(path):
    df = pd.read_csv(path, header=None, sep = "\t")
    df.columns = columns
    return df

datasets = [Dataset(d.name, load_df(MAIN_PATH_DATASET + d.name + '_' + d.train + '.tsv'),
                            load_df(MAIN_PATH_DATASET + d.name + '_' + d.test + '.tsv'))
                            for d in datasets]

# Group targets into complex and non-complex and flatten MWE
targets_complex = set([ngram for ds in datasets 
                    for mwe in ds.train.loc[ds.train['binary'] == 1,]['target'].tolist() 
                    for ngram in mwe.split()])
targets_non_complex = set([ngram for ds in datasets 
                    for mwe in ds.train.loc[ds.train['binary'] == 0,]['target'].tolist() 
                    for ngram in mwe.split()])

# Clean groups from overlapping words
targets_complex_cleaned = list(targets_complex.difference(targets_non_complex))
targets_non_complex_cleaned = list(targets_non_complex.difference(targets_complex))
targets_complex = list(targets_complex)
targets_non_complex = list(targets_non_complex)

# Transform to charater sequences for training character-level Kneser-Ney model
chars_complex = [[character for character in word] 
                    for word in targets_complex]
chars_non_complex = [[character for character in word]
                    for word in targets_non_complex]
chars_complex_cleaned = [[character for character in word]
                    for word in targets_complex_cleaned]
chars_non_complex_cleaned = [[character for character in word]
                    for word in targets_non_complex_cleaned]

all_targets = list([ngram for ds in datasets 
                    for mwe in ds.train['target'].tolist() 
                    for ngram in mwe.split()])

print('Len ta_complex : {}'.format(len(targets_complex)))
print('Len ta_non_complex : {}'.format(len(targets_non_complex)))
print('Len ta_complex_cleaned : {}'.format(len(targets_complex_cleaned)))
print('Len ta_non_complex_cleaned : {}'.format(len(targets_non_complex_cleaned)))

# Complex unigram, bigram and trigram models
with open('../resources/language-models/ngram_char_1_complex.json', 'wb') as fp:
        ngram_char_1_complex = KneserNeyNGram(n=1, sents=chars_complex, D = 1)
        pickle.dump(ngram_char_1_complex, fp, protocol=pickle.HIGHEST_PROTOCOL)
        
with open('../resources/language-models/ngram_char_2_complex.json', 'wb') as fp:
        ngram_char_2_complex = KneserNeyNGram(n=2, sents=chars_complex, D = 1)
        pickle.dump(ngram_char_2_complex, fp, protocol=pickle.HIGHEST_PROTOCOL)        
        
with open('../resources/language-models/ngram_char_3_complex.json', 'wb') as fp:
        ngram_char_3_complex = KneserNeyNGram(n=3, sents=chars_complex, D = 1)
        pickle.dump(ngram_char_3_complex, fp, protocol=pickle.HIGHEST_PROTOCOL)
        
        
# Non-complex unigram, bigram and trigram models
with open('../resources/language-models/ngram_char_1_non_complex.json', 'wb') as fp:
        ngram_char_1__non_complex = KneserNeyNGram(n=1, sents=chars_non_complex, D = 1)
        pickle.dump(ngram_char_1__non_complex, fp, protocol=pickle.HIGHEST_PROTOCOL)
        
with open('../resources/language-models/ngram_char_2_non_complex.json', 'wb') as fp:
        ngram_char_2_non_complex = KneserNeyNGram(n=2, sents=chars_non_complex, D = 1)
        pickle.dump(ngram_char_2_non_complex, fp, protocol=pickle.HIGHEST_PROTOCOL)        
        
with open('../resources/language-models/ngram_char_3_non_complex.json', 'wb') as fp:
        ngram_char_3_non_complex = KneserNeyNGram(n=3, sents=chars_non_complex, D = 1)
        pickle.dump(ngram_char_3_non_complex, fp, protocol=pickle.HIGHEST_PROTOCOL)
        

# Cleaned complex unigram, bigram and trigram models
with open('../resources/language-models/ngram_char_1_complex_cleaned.json', 'wb') as fp:
        ngram_char_1_complex_cleaned = KneserNeyNGram(n=1, sents=chars_complex_cleaned, D = 1)
        pickle.dump(ngram_char_1_complex_cleaned, fp, protocol=pickle.HIGHEST_PROTOCOL)
        
with open('../resources/language-models/ngram_char_2_complex_cleaned.json', 'wb') as fp:
        ngram_char_2_complex_cleaned = KneserNeyNGram(n=2, sents=chars_complex_cleaned, D = 1)
        pickle.dump(ngram_char_2_complex_cleaned, fp, protocol=pickle.HIGHEST_PROTOCOL)        
        
with open('../resources/language-models/ngram_char_3_complex_cleaned.json', 'wb') as fp:
        ngram_char_3_complex_cleaned = KneserNeyNGram(n=3, sents=chars_complex_cleaned, D = 1)
        pickle.dump(ngram_char_3_complex_cleaned, fp, protocol=pickle.HIGHEST_PROTOCOL)
        
        
# Cleaned non-complex unigram, bigram and trigram models
with open('../resources/language-models/ngram_char_1_non_complex_cleaned.json', 'wb') as fp:
        ngram_char_1_non_complex_cleaned = KneserNeyNGram(n=1, sents=chars_non_complex_cleaned, D = 1)
        pickle.dump(ngram_char_1_non_complex_cleaned, fp, protocol=pickle.HIGHEST_PROTOCOL)
        
with open('../resources/language-models/ngram_char_2_non_complex_cleaned.json', 'wb') as fp:
        ngram_char_2_non_complex_cleaned = KneserNeyNGram(n=2, sents=chars_non_complex_cleaned, D = 1)
        pickle.dump(ngram_char_2_non_complex_cleaned, fp, protocol=pickle.HIGHEST_PROTOCOL)        
        
with open('../resources/language-models/ngram_char_3_non_complex_cleaned.json', 'wb') as fp:
        ngram_char_3_non_complex_cleaned = KneserNeyNGram(n=3, sents=chars_non_complex_cleaned, D = 1)
        pickle.dump(ngram_char_3_non_complex_cleaned, fp, protocol=pickle.HIGHEST_PROTOCOL)