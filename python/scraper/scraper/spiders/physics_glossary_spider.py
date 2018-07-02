import scrapy

class GlossarySpider(scrapy.Spider):
    name = "physics_glossary"
    start_urls = ['http://www.etutorphysics.com/glossary_{}.html'.format(str(chr(index))) 
    				for index in range(66,90)]
    start_urls.append('http://www.etutorphysics.com/glossary.html')
    
    def parse(self, response):
    	glosses= response.xpath('//b/text()').extract()
    	print(glosses)
    	for gloss in glosses:
    		gloss = ' '.join(gloss.split())
    		gloss = gloss.replace(':','').strip().lower()
    		if gloss:
    			yield {'name' : gloss}
    
              
