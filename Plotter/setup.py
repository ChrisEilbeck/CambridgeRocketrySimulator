## Copyright (C) 2010 S.Box
## setup.py

## Author: S.Box
## Created: 2011-10-27

__author__="simon"
__date__ ="$29-Jul-2011 13:02:42$"

from setuptools import setup,find_packages

setup (
  name = 'RockPlot',
  version = '0.1',
  packages = find_packages(),

  # Declare your packages' dependencies here, for eg:
  install_requires=['foo>=3'],

  # Fill in these to make your Egg ready for upload to
  # PyPI
  author = 'simon',
  author_email = '',

  summary = 'Just another Python package for the cheese shop',
  url = '',
  license = '',
  long_description= 'Long description of the package',

  # could also include long_description, download_url, classifiers, etc.


)
