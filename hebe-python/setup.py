import setuptools
import uonet_request_signer_hebe

with open("README.md") as f:
    long_description = f.read()

setuptools.setup(
    name=uonet_request_signer_hebe.__name__,
    version=uonet_request_signer_hebe.__version__,
    description="UONET+ (hebe) request signer for Python",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/wulkanowy/uonet-request-signer",
    author="Wulkanowy",
    author_email="wulkanowyinc@gmail.com",
    maintainer="Kuba Szczodrzyński",
    maintainer_email="kuba@szczodrzynski.pl",
    license="MIT",
    classifiers=[
        "Development Status :: 4 - Beta",
        "Intended Audience :: Developers",
        "License :: OSI Approved :: MIT License",
        "Natural Language :: English",
        "Operating System :: OS Independent",
        "Programming Language :: Python :: 3.8",
        "Topic :: Education",
        "Topic :: Internet :: WWW/HTTP",
        "Topic :: Security :: Cryptography",
        "Topic :: Software Development :: Libraries :: Python Modules",
    ],
    packages=setuptools.find_packages(),
    install_requires=["pyopenssl"],
    extras_require={"testing": ["pytest"]},
)
