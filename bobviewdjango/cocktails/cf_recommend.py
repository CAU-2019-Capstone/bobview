from math import sqrt
import numpy as np
import pandas as pd

'''
raw = pd.read_csv('..//cocktail_user_survey.csv')

rate = {}  # raw.columns.size = 62 (user size + 1)

#first = 0

for i in range(raw.columns.size):
    first = 0
    for c in range(len(raw)-1):
        if raw.iloc[c,i] != 0 : break
        first += 1
    if raw.iloc[first,i] != 0:
        rate[raw.columns[i]] = {raw.user_id[first] : raw.iloc[first,i]}

for t in range(raw.columns.size-1):
    for i in range(raw.user_id.size):
        if raw.iloc[i,t+1] == 0 : continue
        rate[raw.columns[t+1]][raw.user_id[i]] = raw.iloc[i,t+1]

del rate['user_id']

print(rate)
'''

class RecommenderCF:
    # Euclidean Distance Score
    rate = []
    itemsim = {}
    itemPrefs = {}

    def __init__(self, rate):
        self.rate = rate
        #print(rate)
        self.itemsim=self.calculateSimilarItems()
        #print(self.itemsim)

    # Euclidean Distance Score
    def sim_distance(self ,prefs, person1, person2):
        si={}
        for item in prefs[person1]:
            if item in prefs[person2]:
                si[item]=1
        
        if len(si)==0: return 0
        sum_of_squares=sum([pow(prefs[person1][item]-prefs[person2][item],2)
                        for item in prefs[person1] if item in prefs[person2]])
        return 1/(1+sum_of_squares)

    def sim_pearson(self, prefs, p1, p2):
        si={}
        for item in prefs[p1]:
            if item in prefs[p2]:si[item]=1
                
        n=len(si)
        
        if n==0: return 0
        # Add up all the preferences
        sum1=sum([prefs[p1][it] for it in si])
        sum2=sum([prefs[p2][it] for it in si])
        
        #Sum up the squares
        sum1Sq=sum([pow(prefs[p1][it],2) for it in si])
        sum2Sq=sum([pow(prefs[p2][it],2) for it in si])
        #Sum up the products
        pSum=sum([prefs[p1][it] * prefs[p2][it] for it in si])
        
        #Calculate Pearson score
        num=pSum-(sum1*sum2/n)
        den=sqrt(sum1Sq-pow(sum1,2)/n)*(sum2Sq-pow(sum2,2)/n)
        if den==0:return 0
        r=num/den
        return r

    def transformPrefs(self):
        result={}
        for person in self.rate:
            for item in self.rate[person]:
                result.setdefault(item,{})
                
                result[item][person]=self.rate[person][item]
        return result

    def topMatches(self,person,n=5, similarity=sim_distance):
        
        scores=[(similarity(self.itemPrefs,person,other),other)
        for other in self.itemPrefs if other!=person]
        
        scores.sort()
        scores.reverse()
        return scores[0:n]

    def calculateSimilarItems(self, n=10):
        result = {}
        self.itemPrefs=self.transformPrefs()
        c=0
        for item in self.itemPrefs:
            c+=1
            if c%100 == 0: print("%d / %d" %(c, len(self.itemPrefs)))
            # Find the most similar items to this one\
            scores=self.topMatches(item,n=n, similarity=self.sim_pearson)
            result[item]=scores
        return result

    # User-based cf
    # Gets recommendations for a person by using a weighted average
    # of every oher user's rankings
    def getRecommendUserBased(self,person, similarity=sim_pearson):
        totals={}
        simSums={}
        for other in self.rate:
            if other==person: continue
            sim=similarity(self, self.rate, person,other)
            #print(sim)
            # ignore scores of zero or lower
            if sim<=0: continue
            for item in self.rate[other]:
                
                # only scores cocktails I haven't drink yet
                if item not in self.rate[person] or self.rate[person][item]==0:
                    # Similarity * Score
                    totals.setdefault(item,0)
                    totals[item]+=self.rate[other][item]*sim
                    # Sum of similarities
                    simSums.setdefault(item,0)
                    simSums[item]+=sim
                    
        # Create the normalized list
        rankings=[(total/simSums[item],item) for item,total in totals.items()]
        
        # Return the sorted list
        rankings.sort()
        rankings.reverse()

        iter = 0
        for i in range(len(rankings)):
            if rankings[i][0] < 0.5:
                del rankings[i]
                iter = i
                break
        for i in range(len(rankings)-iter) : del rankings[iter]
        return rankings

    # Item-based cf
    def getRecommendItemBased(self,user):
        userRatings=self.rate[user]
        scores={}
        totalSim={}

        # Loop over items rated by this user
        for (item,rating) in userRatings.items():
            # Loop over items similar to this one
            for (similarity, item2) in self.itemsim[item]:
                # Ignore if this user has already rated this item
                if item2 in userRatings:continue
                # Weighted sum of rating times similarity
                scores.setdefault(item2,0)
                scores[item2]+=similarity*rating
                # Sum of all the similarities
                totalSim.setdefault(item2,0)
                totalSim[item2]+=similarity
            

        # Divide each total score by total weighting to get an average
        rankings=[]
        for (item, score) in scores.items():
            if totalSim[item] is not 0:
                rankings.append((score/totalSim[item],item))
            else :
                rankings.append((0,item))
            
        # Return the rankings from highest to lowest
        rankings.sort()
        rankings.reverse()

        iter = 0
        for i in range(len(rankings)):
            if rankings[i][0] < 0.5:
                del rankings[i]
                iter = i
                break
        for i in range(len(rankings)-iter) : del rankings[iter]

        return rankings

if __name__ == "__main__" : 
    rate = {
        '10': {
            'Dry Martini': 1, 
            'Margarita': 5, 
            'Jin tonic': 5, 
            'Kahlua Milk': 5, 
            'Barcardi': 5, 
            'Screw Driver': 5, 
            'Jägerbomb': 1
        },
        '11': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 1, 'Apple Martini': 5, 'Jin tonic': 5, 'Kahlua Milk': 1, 'Tequila Sunrise': 5, 'Barcardi': 1, 'Whiskey sour': 5, 'Screw Driver': 5, 'Midori sour': 5, 'Jack Coke': 5, 'Jägerbomb': 5}, 
        '12': {'Mojito': 5, 'Peach Crush': 5, 'Old Fashioned': 5, 'Blue Hawaii': 1, 'Long Island Tea': 5, 'Dry Martini': 5, 'Apple Martini': 1, 'Margarita': 5, 'Manhattan': 5, 'Jin tonic': 5, 'Cosmopolitan': 1, 'Kahlua Milk': 1, 'June Bug': 5, 'Tequila Sunrise': 5, 'Barcardi': 1, 'Vodca & tonic': 1, 'Whiskey sour': 5, 'God Father': 5, 'Screw Driver': 5, 'Midori sour': 5, 'Jack Coke': 5, 'Jägerbomb': 1},
        '13': {'Mojito': 5, 'Peach Crush': 5, 'Old Fashioned': 1, 'Blue Hawaii': 5, 'Apple Martini': 5, 'Jin tonic': 5, 'Kahlua Milk': 5, 'June Bug': 1, 'Tequila Sunrise': 1, 'Barcardi': 5, 'Vodca & tonic': 5, 'Whiskey sour': 5, 'Jack Coke': 5}, 
        '14': {'Jack Coke': 1}, 
        '15': {'Mojito': 5, 'Peach Crush': 5, 'Old Fashioned': 5, 'Blue Hawaii': 1, 'Long Island Tea': 1, 'Dry Martini': 5, 'Apple Martini': 1, 'Margarita': 5, 'Manhattan': 5, 'Jin tonic': 5, 'Kahlua Milk': 1, 'June Bug': 1, 'Tequila Sunrise': 5, 'Barcardi': 1, 'Vodca & tonic': 1, 'Whiskey sour': 5, 'God Father': 5, 'Screw Driver': 5, 'Midori sour': 1, 'Jack Coke': 5, 'Jägerbomb': 1}, 
        '16': {'Mojito': 5, 'Blue Hawaii': 5, 'Margarita': 5, 'Jin tonic': 5, 'Kahlua Milk': 5, 'Barcardi': 1, 'Vodca & tonic': 5, 'Jack Coke': 1}, 
        '17': {'Old Fashioned': 5, 'God Father': 5, 'Jack Coke': 5}, 
        '18': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 5, 'Dry Martini': 5, 'Apple Martini': 5, 'Margarita': 5, 'Jin tonic': 5, 'Vodca & tonic': 5}, 
        '19': {'Blue Hawaii': 5, 'Jin tonic': 5, 'Kahlua Milk': 1, 'Vodca & tonic': 1, 'Whiskey sour': 5, 'God Father': 5, 'Screw Driver': 5, 'Jack Coke': 5}, 
        '21': {'Mojito': 5, 'Peach Crush': 5, 'Old Fashioned': 5, 'Blue Hawaii': 5, 'Long Island Tea': 5, 'Dry Martini': 5, 'Apple Martini': 5, 'Margarita': 1, 'Manhattan': 1, 'Jin tonic': 5, 'Cosmopolitan': 5, 'Kahlua Milk': 1, 'Tequila Sunrise': 1, 'Barcardi': 1, 'Vodca & tonic': 5, 'God Father': 5, 'Screw Driver': 5, 'Midori sour': 5, 'Jack Coke': 5, 'Jägerbomb': 5}, '22': {'Mojito': 5, 'Peach Crush': 5, 'Old Fashioned': 5, 'Blue Hawaii': 5, 'Kahlua Milk': 5, 'Vodca & tonic': 1, 'Screw Driver': 5, 'Midori sour': 1, 'Jack Coke': 5}, 
        '23': {'Mojito': 5, 'Peach Crush': 5, 'Old Fashioned': 5, 'Blue Hawaii': 5, 'Long Island Tea': 5, 'Dry Martini': 5, 'Apple Martini': 5, 'Margarita': 5, 'Manhattan': 5, 'Jin tonic': 5, 'Cosmopolitan': 5, 'Kahlua Milk': 5, 'June Bug': 5, 'Tequila Sunrise': 5, 'Barcardi': 5, 'Vodca & tonic': 5, 'Whiskey sour': 5, 'God Father': 5, 'Screw Driver': 5, 'Midori sour': 5, 'Jack Coke': 5, 'Jägerbomb': 5}, '25': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 1, 'Long Island Tea': 5, 'Margarita': 5, 'Cosmopolitan': 5, 'Kahlua Milk': 5, 'Tequila Sunrise': 5, 'Midori sour': 1}, 
        '26': {'Mojito': 5, 'Blue Hawaii': 5, 'Dry Martini': 1, 'Jin tonic': 5, 'Kahlua Milk': 5, 'Barcardi': 5, 'Vodca & tonic': 5, 'Jack Coke': 5, 'Jägerbomb': 5}, 
        '27': {'Mojito': 1, 'Peach Crush': 5, 'Blue Hawaii': 5, 'Long Island Tea': 5, 'Dry Martini': 1, 'Apple Martini': 5, 'Margarita': 1, 'Manhattan': 1, 'Jin tonic': 5, 'Cosmopolitan': 1, 'Kahlua Milk': 1, 'June Bug': 1, 'Tequila Sunrise': 5, 'Barcardi': 1, 'Vodca & tonic': 5, 'Whiskey sour': 5, 'God Father': 1, 'Screw Driver': 1, 'Midori sour': 5, 'Jack Coke': 1, 'Jägerbomb': 5}, '28': {'Mojito': 5, 'Peach Crush': 5, 'Old Fashioned': 5, 'Blue Hawaii': 5, 'Long Island Tea': 5, 'Margarita': 5, 'Jin tonic': 5, 'Kahlua Milk': 5, 'June Bug': 5, 'Tequila Sunrise': 1, 'Vodca & tonic': 5, 'Whiskey sour': 1, 'God Father': 5, 'Screw Driver': 5, 'Jack Coke': 5, 'Jägerbomb': 1}, 
        
    }
    '''
    rate = {'1': {98: 5, 188: 5}, '2': {378: 5, 474: 5}, '3': {214: 5, 77: 5}, '4': {272: 5, 79: 5, 87: 5, 424: 5, 47: 5, 70: 5, 344: 5, 372: 5, 286: 5, 71: 5, 413: 5}, '5': {463: 5, 287: 5, 157: 5}, '6': {377: 5, 399: 5, 354: 5, 484: 5, 272: 5, 36: 5}, '8': {265: 5, 158: 5, 446: 5, 232: 5, 349: 5, 520: 5, 248: 5, 463: 5, 233: 5, 525: 5, 465: 5, 466: 5, 200: 5}, '11': {503: 5, 544: 5, 6: 5, 213: 5, 94: 5, 8: 5, 445: 5, 364: 5, 262: 5}, '12': {40: 5, 282: 5, 129: 5, 501: 5, 510: 5}, '13': {115: 5, 272: 5, 493: 5, 232: 5, 52: 1, 510: 1}, '14': {174: 5, 57: 5, 455: 5, 207: 1, 83: 1}, '15': {40: 5, 111: 5, 131: 5, 222: 5, 52: 1, 510: 1}, '16': {40: 5, 111: 5, 52: 1, 510: 1}, '17': {40: 5, 111: 5, 131: 5, 222: 5, 333: 5, 444: 5, 512: 5, 52: 1, 510: 1, 443: 1}}



    
        
        '29': {'Mojito': 5, 'Peach Crush': 5, 'Old Fashioned': 5, 'Blue Hawaii': 5, 'Long Island Tea': 5, 'Dry Martini': 5, 'Apple Martini': 5, 'Margarita': 5, 'Manhattan': 5, 'Jin tonic': 5, 'Cosmopolitan': 5, 'Kahlua Milk': 5, 'June Bug': 5, 'Tequila Sunrise': 5, 'Barcardi': 5, 'Vodca & tonic': 5, 'Whiskey sour': 5, 'God Father': 5, 'Screw Driver': 5, 'Midori sour': 5, 'Jack Coke': 1, 'Jägerbomb': 5}, '30': {'Mojito': 5, 'Peach Crush': 5, 'Old Fashioned': 5, 'Long Island Tea': 5, 'Dry Martini': 5, 'Apple Martini': 5, 'Margarita': 5, 'Manhattan': 5, 'Jin tonic': 5, 'Cosmopolitan': 5, 'Kahlua Milk': 5, 'June Bug': 5, 'Tequila Sunrise': 5, 'Barcardi': 5, 'Vodca & tonic': 5, 'Whiskey sour': 5, 'God Father': 5, 'Screw Driver': 5, 'Midori sour': 5, 'Jack Coke': 5, 'Jägerbomb': 5}, '31': {'Mojito': 5, 'Blue Hawaii': 5, 'Kahlua Milk': 5, 'Jägerbomb': 5}, '32': {'Mojito': 5, 'Peach Crush': 5, 'Old Fashioned': 1, 'Blue Hawaii': 1, 'Long Island Tea': 1, 'Dry Martini': 5, 'Apple Martini': 1, 'Margarita': 1, 'Manhattan': 1, 'Jin tonic': 1, 'Cosmopolitan': 5, 'Kahlua Milk': 1, 'Tequila Sunrise': 5, 'Vodca & tonic': 1, 'Whiskey sour': 5, 'God Father': 1, 'Screw Driver': 5, 'Midori sour': 5, 'Jack Coke': 5, 'Jägerbomb': 1}, '33': {'Peach Crush': 5, 'Apple Martini': 5, 'Margarita': 5, 'Manhattan': 5, 'Jin tonic': 5, 'Tequila Sunrise': 5, 'Vodca & tonic': 5, 'God Father': 5, 'Screw Driver': 5, 'Jack Coke': 5}, '34': {'Mojito': 5, 'Blue Hawaii': 5, 'Apple Martini': 5, 'Jin tonic': 5, 'Whiskey sour': 5, 'Jägerbomb': 5}, '35': {'Mojito': 5, 'Peach Crush': 1, 'Blue Hawaii': 1, 'Long Island Tea': 5, 'Dry Martini': 5, 'Apple Martini': 5, 'Margarita': 5, 'Jin tonic': 5, 'Cosmopolitan': 1, 'Kahlua Milk': 1, 'June Bug': 5, 'Tequila Sunrise': 1, 'Barcardi': 5, 'Vodca & tonic': 1, 'God Father': 5, 'Screw Driver': 1, 'Midori sour': 5, 'Jack Coke': 5, 'Jägerbomb': 1}, '36': {'Mojito': 5, 'Dry Martini': 1, 'Manhattan': 5, 'Jin tonic': 5, 'Kahlua Milk': 5, 'Vodca & tonic': 5, 'God Father': 5, 'Screw Driver': 5, 'Jack Coke': 1, 'Jägerbomb': 5}, '37': {'Mojito': 5, 'Peach Crush': 5, 'Old Fashioned': 5, 'Long Island Tea': 1, 'Dry Martini': 5, 'Apple Martini': 5, 'Margarita': 1, 'Manhattan': 5, 'Jin tonic': 1, 'Kahlua Milk': 1, 'Tequila Sunrise': 1, 'Barcardi': 1, 'Vodca & tonic': 1, 'Whiskey sour': 5, 'God Father': 1, 'Screw Driver': 1, 'Midori sour': 5, 'Jack Coke': 1, 'Jägerbomb': 1}, '38': {'Mojito': 5, 'Dry Martini': 1, 'Jin tonic': 5, 'June Bug': 1, 'Vodca & tonic': 5, 'Jägerbomb': 5}, '39': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 5, 'Kahlua Milk': 5, 'Vodca & tonic': 5, 'Jack Coke': 5}, '41': {'Mojito': 5, 'Peach Crush': 5, 'Apple Martini': 5, 'Jin tonic': 5, 'Kahlua Milk': 5, 'Tequila Sunrise': 1, 'Barcardi': 1, 'Vodca & tonic': 1, 'Jägerbomb': 1}, '42': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 5, 'Long Island Tea': 1, 'Dry Martini': 1, 'Apple Martini': 1, 'Margarita': 1, 'Jin tonic': 5, 'Cosmopolitan': 5, 'Kahlua Milk': 5, 'June Bug': 5, 'Tequila Sunrise': 5, 'Vodca & tonic': 5, 'Midori sour': 5, 'Jack Coke': 5, 'Jägerbomb': 5}, '43': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 5, 'Apple Martini': 5, 'Margarita': 5, 'Jin tonic': 5, 'Cosmopolitan': 1, 'Kahlua Milk': 5, 'Barcardi': 1, 'Vodca & tonic': 5, 'Midori sour': 5, 'Jägerbomb': 1}, '44': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 5, 'Long Island Tea': 5, 'Apple Martini': 5, 'Kahlua Milk': 1, 'Screw Driver': 5}, '45': {'Mojito': 1, 'Peach Crush': 5, 'Old Fashioned': 1, 'Blue Hawaii': 5, 'Long Island Tea': 5, 'Dry Martini': 1, 'Apple Martini': 5, 'Margarita': 5, 'Manhattan': 5, 'Jin tonic': 1, 'Cosmopolitan': 5, 'Kahlua Milk': 5, 'Tequila Sunrise': 5, 'Barcardi': 5, 'Vodca & tonic': 5, 'Whiskey sour': 1, 'Screw Driver': 5, 'Jack Coke': 5, 'Jägerbomb': 5}, '47': {'Peach Crush': 5, 'Kahlua Milk': 5, 'Vodca & tonic': 5, 'Midori sour': 1, 'Jack Coke': 5}, '48': {'Mojito': 5, 'Peach Crush': 1, 'Blue Hawaii': 1, 'Long Island Tea': 1, 'Margarita': 5, 'Jin tonic': 5, 'Kahlua Milk': 5, 'Tequila Sunrise': 5, 'Barcardi': 1, 'Vodca & tonic': 5, 'Screw Driver': 1, 'Jack Coke': 5, 'Jägerbomb': 5}, '49': {'Margarita': 1, 'Cosmopolitan': 1}, '51': {'Mojito': 5, 'Peach Crush': 5, 'Old Fashioned': 1, 'Blue Hawaii': 5, 'Long Island Tea': 5, 'Dry Martini': 1, 'Apple Martini': 5, 'Margarita': 5, 'Manhattan': 5, 'Jin tonic': 1, 'Cosmopolitan': 5, 'Kahlua Milk': 5, 'June Bug': 1, 'Tequila Sunrise': 5, 'Barcardi': 1, 'Vodca & tonic': 1, 'Whiskey sour': 1, 'God Father': 1, 'Screw Driver': 5, 'Midori sour': 1, 'Jack Coke': 5, 'Jägerbomb': 5}, '52': {'Manhattan': 5, 'Jin tonic': 1, 'Tequila Sunrise': 1, 'Vodca & tonic': 5, 'Jägerbomb': 5}, '53': {'Peach Crush': 1, 'Vodca & tonic': 1, 'Whiskey sour': 1}, '54': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 1, 'Apple Martini': 5, 'Cosmopolitan': 5, 'Kahlua Milk': 5}, '55': {'Mojito': 5, 'Peach Crush': 5, 'Old Fashioned': 5, 'Blue Hawaii': 1, 'Long Island Tea': 5, 'Dry Martini': 5, 'Apple Martini': 5, 'Margarita': 5, 'Manhattan': 5, 'Jin tonic': 5, 'Cosmopolitan': 5, 'Kahlua Milk': 5, 'June Bug': 5, 'Tequila Sunrise': 5, 'Barcardi': 5, 'Vodca & tonic': 5, 'Whiskey sour': 5, 'Screw Driver': 5, 'Midori sour': 5, 'Jägerbomb': 5}, '56': {'Mojito': 5, 'Blue Hawaii': 5, 'Long Island Tea': 5, 'Midori sour': 5}, '57': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 5, 'Dry Martini': 5, 'Jin tonic': 5, 'Cosmopolitan': 5, 'Kahlua Milk': 5, 'Tequila Sunrise': 5, 'Vodca & tonic': 5, 'Screw Driver': 5, 'Jack Coke': 5}, '58': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 5, 'Long Island Tea': 5, 'Apple Martini': 5, 'Jin tonic': 5, 'Kahlua Milk': 1, 'June Bug': 5, 'God Father': 5, 'Midori sour': 5, 'Jack Coke': 5, 'Jägerbomb': 1}, '59': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 1, 'Long Island Tea': 5, 'Jin tonic': 5, 'Kahlua Milk': 5, 'June Bug': 5, 'Vodca & tonic': 5, 'Screw Driver': 1, 'Midori sour': 5, 'Jack Coke': 5, 'Jägerbomb': 5},
        '60': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 5, 'Apple Martini': 5, 'Jin tonic': 1, 'Kahlua Milk': 5, 'Vodca & tonic': 1, 'Midori sour': 5, 'Jägerbomb': 1}, 
        '61': {'Blue Hawaii': 5}, 
        '62': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 5, 'Jin tonic': 5, 'Kahlua Milk': 5, 'Barcardi': 5, 'Vodca & tonic': 5, 'Midori sour': 5, 'Jägerbomb': 5}, 
        '63': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 5, 'Apple Martini': 5, 'Kahlua Milk': 5, 'Jägerbomb': 5}, 
        '64': {'Blue Hawaii': 5, 'Jin tonic': 5, 'Kahlua Milk': 5, 'Vodca & tonic': 5}, 
        '65': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 5, 'Kahlua Milk': 5, 'June Bug': 5, 'Screw Driver': 5, 'Midori sour': 5, 'Jägerbomb': 5}, 
        '66': {'Mojito': 5, 'Jin tonic': 5, 'Kahlua Milk': 1, 'June Bug': 5, 'Tequila Sunrise': 5, 'Vodca & tonic': 5, 'Screw Driver': 5, 'Midori sour': 5, 'Jack Coke': 5, 'Jägerbomb': 1}, 
        '67': {'Mojito': 5, 'Peach Crush': 1, 'Old Fashioned': 1, 'Dry Martini': 5, 'Apple Martini': 1, 'Margarita': 5, 'Jin tonic': 5, 'Cosmopolitan': 1, 'Kahlua Milk': 1, 'Tequila Sunrise': 1, 'Barcardi': 1, 'Vodca & tonic': 1, 'God Father': 1, 'Midori sour': 5, 'Jack Coke': 1}, 
        '68': {'Mojito': 5, 'Peach Crush': 5, 'Long Island Tea': 5, 'June Bug': 5, 'Tequila Sunrise': 5, 'Jack Coke': 1}, 
        '69': {'Mojito': 5, 'Blue Hawaii': 5, 'Tequila Sunrise': 5, 'Jägerbomb': 5}, 
        '70': {'Mojito': 5, 'Peach Crush': 5, 'Blue Hawaii': 5, 'Long Island Tea': 5, 'Dry Martini': 5, 'Apple Martini': 5, 'Margarita': 5, 'Jin tonic': 1, 'Cosmopolitan': 5, 'Kahlua Milk': 1, 'June Bug': 5, 'Tequila Sunrise': 5, 'God Father': 1, 'Screw Driver': 5, 'Midori sour': 5, 'Jack Coke': 5, 'Jägerbomb': 5}
    '''
    r = RecommenderCF(rate)
    print(r.getRecommendUserBased('10'))
    
    print(r.getRecommendItemBased('10'))

