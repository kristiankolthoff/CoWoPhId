import numpy as np
from functools import lru_cache

@lru_cache(maxsize=None)
def missing_strat_random(word, dim):
    return np.random.rand(dim)

def missing_strat_zeros(word, dim):
    return np.zeros(dim)

def ngram_repr_bag_of_words(word_vecs):
    return np.mean(word_vecs, axis = 0)

def ngram_repr_weighted_bag_of_word(word_vecs, words, alpha, unigram_prob):
    weighted_vecs = [(alpha/(alpha+unigram_prob.get(word))) \
                for vec, word in zip(word_vecs, words)]
    matrix = np.array(weighted_vecs)
    u, s, vh = np.linalg.svd(matrix, full_matrices=True)
    vec_mean = np.mean(weighted_vecs, axis = 0)
    singular_vec = u[:0]
    vec_final = vec_mean - (singular_vec * \
                        np.transpose(singular_vec) * vec_mean)
    return vec_final