from bootstrapped_lexicon import bootstrap_lexicon_simple_norm
from bootstrapped_lexicon import missing_strat_random
from bootstrapped_lexicon import ngram_embedding_similarity
from bootstrapped_lexicon import ngram_repr_bag_of_words
import gensim


model = gensim.models.KeyedVectors.load_word2vec_format('resources/' + \
            'word-embeddings/GoogleNews-vectors-negative300.bin', binary=True)

seeds_complex = ['aboriginal']
seeds_non_complex = ['bad']

# Build the vocabulary
vocabulary = []
vocabulary.extend(seeds_complex)
vocabulary.extend(seeds_non_complex)
vocabulary.extend(['good', 'Inuit'])

print('---------Complex Seeds----------------')
print(seeds_complex)
print('---------Non-Complex Seeds----------------')
print(seeds_non_complex)
print('---------Vocabulary----------------')
print(vocabulary)

lexicon = bootstrap_lexicon_simple_norm(model, vocabulary, seeds_non_complex, seeds_complex, \
                  ngram_embedding_similarity, missing_strat_random, \
                  ngram_repr_bag_of_words, epochs=3, thresh_l=-0.3, thresh_r=0.4)