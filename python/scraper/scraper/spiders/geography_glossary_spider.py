import scrapy

class GlossarySpider(scrapy.Spider):
    name = "geography_glossary"
    start_urls = ['http://www.physicalgeography.net/physgeoglos/{}.html'.format(str(chr(index))) 
    				for index in range(97,123)]
    
    def parse(self, response):
    	glosses= response.xpath('//a/following-sibling::b/text()').extract()
    	for gloss in glosses:
    		gloss = ' '.join(gloss.split())
    		gloss = gloss.strip().lower()
    		if gloss:
    			yield {'name' : gloss}
    
              
