import numpy as np
from functools import lru_cache

@lru_cache(maxsize=None)
def missing_strat_random(word, dim):
    return np.random.rand(dim)

def missing_strat_zeros(word, dim):
    return np.zeros(dim)

def ngram_repr_bow_mean(word_vecs, *args):
    return np.mean(word_vecs, axis = 0)

def ngram_repr_bow_median(word_vecs, *args):
    return np.median(word_vecs, axis = 0)

def ngram_repr_bow_max(word_vecs, *args):
    return np.max(word_vecs, axis = 0)

def ngram_repr_bow_min(word_vecs, *args):
    return np.min(word_vecs, axis = 0)

def ngram_repr_bow_concat(word_vecs, ngram_bow = [ngram_repr_bow_max]):
    return np.concatenate([ngram_repr(word_vecs) for ngram_repr in ngram_bow])

def ngram_repr_weighted_bow(word_vecs, words, alpha, unigram_prob, ngram_repr):
    weighted_vecs = [(alpha/(alpha+unigram_prob(word))) * vec \
                for vec, word in zip(word_vecs, words)]
    return ngram_repr(weighted_vecs)  

#word_freq_wiki = {}
#sum_counts = 0
#with open("resources/word-freq-dumps/enwiki-20150602-words-frequency.txt", encoding="utf8") as file:
#    for line in file:
#        word, freq = line.partition(" ")[::2]
#        sum_counts+=int(freq)
#        word_freq_wiki[word.strip()] = int(freq)
#        
#def get_unigram_probability(word):
#    return word_freq_wiki[word] / sum_counts
        

#from gensim.test.utils import datapath, get_tmpfile
#from gensim.models import KeyedVectors
#from gensim.scripts.glove2word2vec import glove2word2vec
#
#MAIN_PATH = 'D:/workspace_python/CoWoReId/python/resources/word-embeddings/'
#glove_file = datapath(MAIN_PATH + 'glove.6B.200d.txt')
#tmp_file = get_tmpfile("glove2word2vec.txt")
#glove2word2vec(glove_file, tmp_file)
#
#model_glove = KeyedVectors.load_word2vec_format(tmp_file)
