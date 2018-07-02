import csv

from scrapy.exporters import CsvItemExporter


class QuoteAllDialect(csv.excel):
    quoting = csv.QUOTE_ALL


class QuoteAllCsvItemExporter(CsvItemExporter):

    def __init__(self, *args, **kwargs):
        kwargs.update({'dialect': QuoteAllDialect})
        super(QuoteAllCsvItemExporter, self).__init__(*args, **kwargs)