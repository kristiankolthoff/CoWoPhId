import scrapy

class GlossarySpider(scrapy.Spider):
    name = "biology_glossary"
    start_urls = ['http://www.phschool.com/science/biology_place/glossary/{}.html'.format(str(chr(index))) 
    				for index in range(97,123)]
    
    def parse(self, response):
    	glosses= response.xpath('//div[@id="main"]/h4/text()').extract()
    	for gloss in glosses:
    		index = (gloss.find('(')) if gloss.find('(') != -1 else len(gloss)
    		gloss = gloss[:index]
    		gloss = gloss.lower()
    		gloss = ' '.join(gloss.split())
    		yield {'name' : gloss}
    
              
