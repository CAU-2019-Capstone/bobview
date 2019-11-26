import pymysql
import csv

fs = open('/home/hyuncho/workspace/all_cocktails.csv')
rdr = csv.reader(fs)


db = pymysql.connect(
    host='database-capstone-cau-2019.ci3rms0w5cuf.ap-northeast-2.rds.amazonaws.com', 
    port=3306, 
    user='bobview', 
    passwd='qwer1234!', 
    db='bobview', 
    charset='utf8'
    )

cursor = db.cursor()

sql = """   insert into cocktail(cocktail_id, name, alcoholic, 
            category, glass, instruction, drink_image)
            values (%s, %s, %s, %s, %s, %s, %s)"""

alcoholic = False

for line in rdr :
    if(line[0] != ''):
        if(line[2] == "Alcoholic"):
            alcoholic = True
        cursor.execute(sql, (line[0], line[1],alcoholic, line[3], line[4], line[16], line[17]))
        print(line[0])
        print(line[1])
        print(alcoholic)
        print(line[3])
        print(line[4])
        print(line[16])
        print(line[17])

db.commit()

db.close()