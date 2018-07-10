import pandas as pd
import gensim
from bootstrapped_lexicon import bootstrap_lexicon
from bootstrapped_lexicon import bootstrap_lexicon_simple_norm
from bootstrapped_lexicon import missing_strat_random
from bootstrapped_lexicon import ngram_embedding_similarity
from bootstrapped_lexicon import ngram_repr_bag_of_words

#model = gensim.models.KeyedVectors.load_word2vec_format('resources/' + \
#            'word-embeddings/GoogleNews-vectors-negative300.bin', binary=True)

TRAIN_ENGLISH_WIKIPEDIA = "../cwishareddataset/traindevset/" + \
                           "english/Wikipedia_Train.tsv"
TEST_ENGLISH_WIKIPEDIA = "../cwishareddataset/testset/" + \
                           "english/Wikipedia_Test.tsv"
df = pd.read_csv(TRAIN_ENGLISH_WIKIPEDIA, sep = "\t")
df_test = pd.read_csv(TEST_ENGLISH_WIKIPEDIA, sep = '\t')
df.columns = ['id', 'sentence', "start", "end", "target", 
              "nat", "non_nat", "nat_marked", "non_nat_marked", "binary", "prob"]
df_test.columns = ['id', 'sentence', "start", "end", "target", 
              "nat", "non_nat", "nat_marked", "non_nat_marked", "binary", "prob"]

df['num_tokens'] = df.target.apply(lambda target : len(target.split()))

# Get the basic seeds for complex and non_complex words

#seeds_complex = [word.lower().strip() for word in df.loc[df['binary'] == 1,'target'].tolist()]
#seen = set()
#seen_add = seen.add
#seeds_complex = [x for x in seeds_complex if not (x in seen or seen_add(x))]


#seeds_non_complex = [word.lower().strip() for word in df.loc[df['binary'] == 0,'target'].tolist()]
#seen = set()
#seen_add = seen.add
#seeds_non_complex = [x for x in seeds_non_complex if not (x in seen or seen_add(x))]

seeds_complex = ['aboriginal']
seeds_non_complex = ['bad']

# Build the vocabulary
vocabulary = []
vocabulary.extend(seeds_complex)
vocabulary.extend(seeds_non_complex)
vocabulary.extend(['good', 'Inuit'])
#vocabulary.extend([ngram.strip().lower() for target in df_test['target'].tolist() \
 #                   for ngram in target.split()])
#ref = 'and'
#vocabulary.append(ref)
print('---------Complex Seeds----------------')
print(len(seeds_complex))
print('---------Non-Complex Seeds----------------')
print(len(seeds_non_complex))
print('---------Vocabulary----------------')
print(len(vocabulary))

lexicon = bootstrap_lexicon_simple_norm(model, vocabulary, seeds_non_complex, seeds_complex, \
                  ngram_embedding_similarity, missing_strat_random, \
                  ngram_repr_bag_of_words, epochs=3, thresh_l=-0.3, thresh_r=0.4)