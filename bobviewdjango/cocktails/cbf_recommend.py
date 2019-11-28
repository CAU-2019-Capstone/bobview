import numpy as np
import itertools

class RecommenderCBF : 
    rec_list=[]
    count = 20
    def __init__(self, data, count):
        self.result = 0
        self.count = count
        all_rec_list = []
        results = np.load("cocktails/results.npy", allow_pickle=True)
        results = results.tolist()
        idx_to_name = np.load("cocktails/idx_to_name.npy", allow_pickle=True)
        idx_to_name = idx_to_name.tolist()

        # below is the input cocktail id list !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        input_item_list = data   # ['Martini', 'Orange Crush', 'Mojito'] -> [12, 123, 1234]
        rec_item_num = self.count   # can change this

        #print(str(rec_item_num) + ' cocktails similar to ' + str(input_item_list) + ' are...')
        for input_item in input_item_list:
            all_rec_list.append(results[input_item][:rec_item_num])

        count_item = 0
        lap = 0
        threshold = 0.3
        first_count = 0
        while count_item < rec_item_num:
            for idx, recs in enumerate(all_rec_list):
                if lap < len(recs):
                    rec = recs[lap]
                    if rec[1] not in input_item_list and rec[0] >= threshold and rec[1] not in itertools.chain(*(self.rec_list)):
                        self.rec_list.append(rec)  # tuple(score, id)
                        count_item += 1
                        print('Recommended: ' + idx_to_name[int(rec[1])] + '  Score: ' + str(rec[0]))
            if first_count == 1 and prev_count_item == count_item:
                break
            prev_count_item = count_item
            lap += 1
            first_count = 1

        # below is the out cocktail (similarity, id) list !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        self.rec_list.sort(reverse=True)   # sort by high similarity

    def GetResult(self):
        for (key,value) in self.rec_list:
            print(value)
        return self.rec_list


if __name__ == "__main__":
    data = [55,11,123,512, 432,323]
    r = RecommenderCBF(data,5)
    rec_lists  = []
    rec_lists = r.GetResult()
    print(rec_lists)
