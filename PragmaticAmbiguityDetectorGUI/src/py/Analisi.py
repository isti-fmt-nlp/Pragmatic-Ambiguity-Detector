'''
@author: Giuseppe Lipari
'''
import nltk
import sys
from ProgressBar import progressBar
from pygraph.classes.digraph import digraph
sys.path.insert(1, sys.argv[2])
sys.path.insert(1, sys.argv[2]+'irutils')
from irutils.TextFilter import TextFilter
from SentenceNetCreator import SentenceNetCreator
from SentenceNetVisitor import SentenceNetVisitor
from os import listdir
from os.path import isfile, join
from DistanceEvaluators import DistanceEvaluators

'''
Metodo che Crea un grafo da una lista di file di testo usando A*
'''
def file_netvisit(file_list,sentencenetvisitor):
        sent_tokenizer = nltk.data.load('tokenizers/punkt/english.pickle')
        #first we have to get the sentences from the files
        sentences = []
        
        for f in file_list:
            input_file = f
            fp = open(input_file, 'r')
            text = fp.read()
            sentences.extend(sent_tokenizer.tokenize(text))
            fp.close()
            
        text_filter = TextFilter()
        for sentence in sentences:
                filtered_sent = text_filter.filter_all(sentence)
                if len(filtered_sent)>0 :
                        sentencenetvisitor.search_A_star(filtered_sent)

                
                
pathsub1=sys.argv[1] + '/1Subject/'
pathsub2=sys.argv[1] + '/2Subject/'
pathreq=sys.argv[1] + '/Requirements/'
pathres=sys.argv[1] + '/Result/'
distancemethod=sys.argv[3]
createmethod=sys.argv[4]

bar=progressBar()
progress=0.0;
bar.setPercent(int(progress))

fp1 = [ (pathsub1 + f) for f in listdir(pathsub1) if isfile(join(pathsub1,f)) ]
fp2 = [ (pathsub2 + f) for f in listdir(pathsub2) if isfile(join(pathsub2,f)) ]

#Apro il file dei requisiti ed associo un requisito ad ogni riga
path_file_req=pathreq + listdir(pathreq)[0]
#print path_file_req
req_file=open(path_file_req,"r")
reqs = req_file.readlines()
req_file.close()
#print reqs

nreq=len(reqs)+4
print nreq
##terms_filter = TextFilter()
evaluator=DistanceEvaluators()

EDGE_START_WEIGHT = 1.0
OCCURRENCES_POS = 0 # the tuple representing the number of occurrences is the first attribute (position 0) for each edge
OCCURRENCES_VALUE_POS = 1 # the value of the number of occurrences is in position 1 in the tuple ('occurrences', <occurrences_number>)
START_OCCURRENCES_NUM = 1 # the starting value for the number of occurrences, which will be placed in the node label
'''
Net-Create from list of file with SentenceNetCreator createNet
'''
if createmethod == "nopriority" :
        s1 = SentenceNetCreator()
        s1.createNet(fp1)
        n1 = s1.get_net()
        v1 = SentenceNetVisitor(n1, EDGE_START_WEIGHT, START_OCCURRENCES_NUM)
        
        progress+=1
        x=float(progress/nreq)
        bar.setPercent(int(x*100))  

        s2 = SentenceNetCreator()
        s2.createNet(fp2)
        n2 = s2.get_net()
        v2 = SentenceNetVisitor(n2, EDGE_START_WEIGHT, START_OCCURRENCES_NUM)

        progress+=1
        x=float(progress/nreq)
        bar.setPercent(int(x*100))

'''
Start net-Create with visit A-star
'''
if createmethod == "priority" :
        s1 = SentenceNetCreator()
        n1 = s1.get_net()
        v1 = SentenceNetVisitor(n1, EDGE_START_WEIGHT, START_OCCURRENCES_NUM)
        file_netvisit(fp1,v1)
        
        progress+=1
        x=float(progress/nreq)
        bar.setPercent(int(x*100))

        s2 = SentenceNetCreator()
        n2 = s2.get_net()
        v2 = SentenceNetVisitor(n2, EDGE_START_WEIGHT, START_OCCURRENCES_NUM)
        file_netvisit(fp2,v2)

        progress+=1
        x=float(progress/nreq)
        bar.setPercent(int(x*100))
'''
end
'''

overlap_file = open(pathres+"knowledge_overlap.txt","w")
subject1 = [(f) for f in listdir(pathsub1) if isfile(join(pathsub1,f))]
subject2 = [(f) for f in listdir(pathsub2) if isfile(join(pathsub2,f))]
domain='Subject1:' + ", ".join(subject1) + '\nSubject2:' + ", ".join(subject2) + ' \nDistance Method: '+distancemethod+' with Knowledge Method '+createmethod+'\n'
overlap_file.write(domain)

ind=1
overlap=0

for req in reqs:
        if distancemethod == 'jaccard' :
                overlap, subgraph1, subgraph2 = evaluator.jaccard_evaluator(req,s1,s2,v1,v2)
        else:
                overlap, subgraph1, subgraph2 = evaluator.jaccard_evaluator(req,s1,s2,v1,v2)
        shortreq = req[0:20].replace("/", "-")
        shortreq = shortreq.replace("\\", "-")
        SentenceNetCreator.write_subgraph(pathres + 'R%d-'%(ind)+ shortreq + '-Subject1.gv', subgraph1)
        SentenceNetCreator.write_subgraph(pathres + 'R%d-'%(ind)+ shortreq + '-Subject2.gv', subgraph2)
        r='Overlap:R%d-%s\n%.10f\n'%(ind,req[0:30],overlap)
        overlap_file.write(r)
        ind+=1
        progress+=1
        x=float(progress/nreq)
        bar.setPercent(int(x*100))
        del subgraph1
        del subgraph2
overlap_file.close()
#s1.write_graph(pathres + 'Graph-KnowledgeSubject1.gv')
progress+=1
x=float(progress/nreq)
bar.setPercent(int(x*100))
#s2.write_graph(pathres + 'Graph-KnowledgeSubject2.gv')
progress+=1
x=float(progress/nreq)
bar.setPercent(int(x*100))

'''
Calcolo distanza con jaccard
'''
'''
#Ciclo creazione e salvataggio sotto grafi cammini + jaccard
for req in reqs:
        progress+=1
        #print 'Req:' + req
	filtered_sent = terms_filter.filter_all(req)
	#print 'Filter: ' + filtered_sent
	path1, path_weight1 = v1.search_A_star(filtered_sent)
	path2, path_weight2 = v2.search_A_star(filtered_sent)
	path1_tokens = nltk.word_tokenize(path1)
	path2_tokens = nltk.word_tokenize(path2)
	current_subgraph = digraph()
	current_subgraph2 = digraph()

        for index, term in enumerate(path1_tokens):
   		subgraph_req = s1.get_connected_subgraph(term)
   		current_subgraph = s1.get_merged_subgraph(current_subgraph, subgraph_req)
	SentenceNetCreator.write_subgraph(pathres + 'R%d-'%(ind)+ req[0:10] + '-Subject1.gv', current_subgraph)
        x=float(progress/nreq)
        bar.setPercent(int(x*100))
        for index, term in enumerate(path2_tokens):
   		subgraph_req2 = s2.get_connected_subgraph(term)
   		current_subgraph2 = s2.get_merged_subgraph(current_subgraph2, subgraph_req2)
	SentenceNetCreator.write_subgraph(pathres + 'R%d-'%(ind)+ req[0:10] + '-Subject2.gv', current_subgraph2)
        path_subgraph1=' '.join(current_subgraph.nodes())
	path_subgraph2=' '.join(current_subgraph2.nodes())
	overlap= SentenceNetCreator.evaluate_jaccard(s1,path_subgraph1,path_subgraph2,filtered_sent)
        r='R%d Knowledge Overlap\n%.10f\n'%(ind,overlap)
        overlap_file.write(r)
        ind+=1
        progress+=1
        x=float(progress/nreq)
        bar.setPercent(int(x*100))
        
overlap_file.close()
s1.write_graph(pathres + 'Graph-KnowledgeSubject1.gv')
progress+=1
x=float(progress/nreq)
bar.setPercent(int(x*100))
s2.write_graph(pathres + 'Graph-KnowledgeSubject2.gv')
progress+=1
x=float(progress/nreq)
bar.setPercent(int(x*100))
'''


