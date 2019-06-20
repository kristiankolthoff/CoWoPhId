import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from nltk.corpus import wordnet as wn
from nltk.corpus import sentiwordnet as swn
from nltk.wsd import lesk
import nltk

from collections import namedtuple
from collections import defaultdict

Dataset = namedtuple('Dataset', 'name, train, test')
FeatureDataset = namedtuple('FeatureDataset', 'name, fc, agg, train, test')
FeatureCategory = namedtuple('FeatureCategory', 'name, func')
Aggregation = namedtuple('Aggregation', 'name, agg')

pd.set_option('display.max_columns', 500)
pd.set_option('display.max_colwidth', 200)
MAIN_PATH_DATASET = "../cwishareddataset/traindevset/english/"
genres = ['Wikipedia', 'WikiNews', 'News']
datasets = ['Train', 'Test']
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

import spotlight
from joblib import Memory

memory = Memory(location='resources/dbpedia-cache', verbose=0)
@memory.cache
def annotate_dbpedia_spotlight(sentence, confidence=0.0, support=20):
    return spotlight.annotate('https://api.dbpedia-spotlight.org/en/annotate',
                                sentence, confidence=confidence, support=support)

sentences_train = list(set([sentence for ds in datasets for sentence in ds.train['sentence'].unique().tolist()]))
sentences_test = list(set([sentence for ds in datasets for sentence in ds.test['sentence'].unique().tolist()]))
sentences = sentences_train.copy()
sentences.extend(sentences_test)
print('Len ta_train : {}'.format(len(sentences_train)))
print('Len ta_test : {}'.format(len(sentences_test)))
print('Len targets : {}'.format(len(sentences)))

import time

sents_dbpedia_75 = {}
sent_no_annotation = []
for index, sentence in enumerate(sentences):
    print('{} : {}'.format(index, sentence))
    if(index % 100 == 0):
        print('----------Sleeping----------')
        time.sleep(60)
    try:
        sents_dbpedia_75[sentence] = annotate_dbpedia_spotlight(sentence, confidence = 0.75)
    except:
        sent_no_annotation.append(sentence)



from SPARQLWrapper import SPARQLWrapper, JSON
import time

exceptions = []
page_rank = {}
for index, uri in enumerate(uris):
    try:
        print('{} : {}'.format(index, uri))
        if(index % 200 == 0):
            print('----------Sleeping----------')
            time.sleep(60)
        sparql = SPARQLWrapper("http://dbpedia.org/sparql")
        sparql.setQuery("""
            PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>
            PREFIX vrank:<http://purl.org/voc/vrank#>
            PREFIX dbo:<http://dbpedia.org/ontology/>
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            SELECT distinct ?v
            FROM <http://dbpedia.org>
            FROM <http://people.aifb.kit.edu/ath/#DBpedia_PageRank>
            WHERE {
                <"""+ uri +"""> vrank:hasRank/vrank:rankValue ?v.
            }
        """)
        sparql.setReturnFormat(JSON)
        results = sparql.query().convert()
        for result in results["results"]["bindings"]:
            page_rank[uri] = float(result['v']['value'])
    except Exception as e:
        exceptions.append(str(e))