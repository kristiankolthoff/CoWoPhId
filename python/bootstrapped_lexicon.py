import numpy as np

def missing_strat_random(word, dim):
    return np.random.rand(dim)

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
    

def ngram_embedding_similarity(model, word_l, word_r, missing_strat, ngram_repr):
    vecs_l = [model[word] if word in model.vocab 
                  else missing_strat(word, model.vector_size) 
                  for word in word_l.split()]   
    vecs_r = [model[word] if word in model.vocab \
                else missing_strat(word, model.vector_size)
                for word in word_r.split()]
    vec_l = ngram_repr(np.array(vecs_l))
    vec_r = ngram_repr(np.array(vecs_r))
    return np.dot(vec_l,vec_r) / (np.linalg.norm(vec_l) \
            * np.linalg.norm(vec_r))

def bootstrap_lexicon(model, vocab, seeds_l, seeds_r, embedding_sim, \
                      missing_strat, ref_term, ngram_repr, epochs=10, \
                      bound_l=-1, bound_r=1, thresh_l=-0.5,thresh_r=0.5):
    if not all(seed in vocab for seed in seeds_l):
        raise ValueError('Not all left seeds contained in vocabulary')
    if not all(seed in vocab for seed in seeds_r):
        raise ValueError('Not all right seeds contained in vocabulary')
    # 1. Initialize the left and right seeds
    se_l = {seed : bound_l for seed in seeds_l}
    se_r = {seed : bound_r for seed in seeds_r}
    lexicon = se_l.copy()
    lexicon.update(se_r)
    for curr_epoch in range(1,epochs+1):
        # 2. Compute left and right weights
        #print(se_l)
        sum_l = np.abs(np.sum([score for word, score in se_l.items()]))
        sum_r = np.abs(np.sum([score for word, score in se_r.items()]))
        weight_l = sum_r / (sum_r + sum_l)
        weight_r = sum_l / (sum_r + sum_l)
        print(sum_l)
        print(sum_r)
        print('Epoch {} : Se_l_size = {}, Se_r_size = {}, weight_l = {}, weight_r = {},'.format(\
                      curr_epoch, len(se_l), len(se_r), weight_l, weight_r))
        for curr_word in vocab:
            if curr_word in se_l or curr_word in se_r:
                continue
            # Compute the weighted left and right scores and sum them
            score_l = [(weight_l * score * \
                        embedding_sim(model, curr_word, seed, missing_strat, ngram_repr)) \
                        for seed, score in se_l.items()]
            score_r = [(weight_r * score * \
                        embedding_sim(model, curr_word, seed, missing_strat, ngram_repr)) \
                        for seed, score in se_r.items()]
            print('Word : {} = {}'.format(curr_word, score_l))
            print(score_r)
            score = np.sum(score_l) + np.sum(score_r)
            print('final score : {}'.format(score))
            lexicon[curr_word] = score
            #print('{} : {}'.format(curr_word, score))
            # Add word to the seed set if the score is low or high enough
            if score <= thresh_l: se_l[curr_word] = score
            if score >= thresh_r: se_r[curr_word] = score
        #print(lexicon)
    # 3. Compute final scores and normalize them
    sim_ref = lexicon.get(ref_term)
    print('SIM_REF:{}'.format(sim_ref))
    if not sim_ref:
        return ValueError('Reference term {} not found in lexicon'.format(ref_term))
    coll_l = {seed : (score - sim_ref) for seed, score in lexicon.items() \
                if (score - sim_ref) < 0}
    coll_r = {seed : (score - sim_ref) for seed, score in lexicon.items() \
                if (score - sim_ref) > 0}
    max_l = np.max(np.abs([score for _, score in coll_l.items()]))
    max_r = np.max(np.abs([score for _, score in coll_r.items()]))
    #print(coll_l)
    #print(max_l)
    lexicon[ref_term] = lexicon[ref_term] - sim_ref
    for word, score in coll_l.items():
        lexicon[word] = score / max_l
    for word, score in coll_r.items():
        lexicon[word] = score / max_r
    return lexicon


def bootstrap_lexicon_simple_norm(model, vocab, seeds_l, seeds_r, embedding_sim, \
                      missing_strat, ngram_repr, epochs=10, \
                      bound_l=-1, bound_r=1, thresh_l=-0.5,thresh_r=0.5):
    if not all(seed in vocab for seed in seeds_l):
        raise ValueError('Not all left seeds contained in vocabulary')
    if not all(seed in vocab for seed in seeds_r):
        raise ValueError('Not all right seeds contained in vocabulary')
    # 1. Initialize the left and right seeds
    se_l = {seed : bound_l for seed in seeds_l}
    se_r = {seed : bound_r for seed in seeds_r}
    lexicon = se_l.copy()
    lexicon.update(se_r)
    for curr_epoch in range(1,epochs+1):
        # 2. Compute left and right weights
        #print(se_l)
        sum_l = np.abs(np.sum([score for word, score in se_l.items()]))
        sum_r = np.abs(np.sum([score for word, score in se_r.items()]))
        weight_l = sum_r / (sum_r + sum_l)
        weight_r = sum_l / (sum_r + sum_l)
        print(sum_l)
        print(sum_r)
        print('Epoch {} : Se_l_size = {}, Se_r_size = {}, weight_l = {}, weight_r = {},'.format(\
                      curr_epoch, len(se_l), len(se_r), weight_l, weight_r))
        for curr_word in vocab:
            if curr_word in se_l or curr_word in se_r:
                continue
            # Compute the weighted left and right scores and sum them
            score_l = [(weight_l * score * \
                        embedding_sim(model, curr_word, seed, missing_strat, ngram_repr)) \
                        for seed, score in se_l.items()]
            score_r = [(weight_r * score * \
                        embedding_sim(model, curr_word, seed, missing_strat, ngram_repr)) \
                        for seed, score in se_r.items()]
            print('Word : {} = {}'.format(curr_word, score_l))
            print(score_r)
            score = np.sum(score_l) + np.sum(score_r)
            print('final score : {}'.format(score))
            lexicon[curr_word] = score
            #print('{} : {}'.format(curr_word, score))
            # Add word to the seed set if the score is low or high enough
            if score <= thresh_l: se_l[curr_word] = score
            if score >= thresh_r: se_r[curr_word] = score
        #print(lexicon)
    # 3. Compute final scores and normalize them
    coll_l = {seed : score for seed, score in lexicon.items() \
                if score < 0}
    coll_r = {seed : score for seed, score in lexicon.items() \
                if score > 0}
    max_l = np.max(np.abs([score for _, score in coll_l.items()]))
    max_r = np.max(np.abs([score for _, score in coll_r.items()]))
    for word, score in coll_l.items():
        lexicon[word] = score / max_l
    for word, score in coll_r.items():
        lexicon[word] = score / max_r
    return lexicon